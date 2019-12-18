package com.macro.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.macro.mall.dto.resp.QmOrderResponse;
import com.macro.mall.mapper.*;
import com.macro.mall.model.OmsOrderItem;
import com.macro.mall.model.*;
import com.macro.mall.service.UmsMemberService;
import com.macro.mall.dao.OmsOrderDao;
import com.macro.mall.dao.PmsProductDao;
import com.macro.mall.dao.PmsSkuStockDao;
import com.macro.mall.common.CommonResult;
import com.macro.mall.common.MathUtil;
import com.macro.mall.config.QmWalletAppConfig;
import com.macro.mall.dao.PortalOrderItemDao;
import com.macro.mall.domain.CartProduct;
import com.macro.mall.domain.OrderParam;
import com.macro.mall.service.OmsPortalOrderService;
import com.macro.mall.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.macro.mall.util.OkHttpUtil.post;

/**
 * 前台订单管理Service
 * Created by macro on 2018/8/30.
 */
@Service
@Slf4j
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private PortalOrderItemDao orderItemDao;
    @Autowired
    private PmsProductDao pmsProductDao;
    @Autowired
    private PmsSkuStockDao skuStockDao;
    @Autowired
    private QmWalletAppConfig qmWalletAppConfig;
    @Autowired
    private OmsOrderDao orderDao;
    @Override
    public CommonResult generateOrder(OrderParam orderParam) throws Exception {
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        String phone = memberService.getCurrentMallAppMemberPhone();
        //当前下单商品
        CartProduct product = pmsProductDao.getCartProduct(orderParam.getProductId());
        PmsSkuStock skuProduct = skuStockDao.findById(orderParam.getSkuId());
        if (product == null) {
            return CommonResult.failed("商品不存在");
        }
        if (skuProduct == null) {
            return CommonResult.failed("sku不存在");
        }
        if (StringUtils.isEmpty(phone)) {
            return CommonResult.failed("用户手机号不能为空");
        }

        if (orderParam.getPayPeriod() <= 0) {
            return CommonResult.failed("分期数不能为空");
        }
        //生成下单商品信息
        OmsOrderItem addOrderItem = new OmsOrderItem();
        addOrderItem.setProductId(product.getId());
        addOrderItem.setProductName(product.getName());
        addOrderItem.setProductPic(product.getPic());
        if (product.getProductAttributeList() != null && product.getProductAttributeList().size() > 0) {
            addOrderItem.setProductAttr(JSON.toJSONString(product.getProductAttributeList()));
        }
        addOrderItem.setProductBrand(product.getBrandName());
        addOrderItem.setProductSn(product.getProductSn());
        addOrderItem.setProductPrice(skuProduct.getPrice());
        addOrderItem.setProductQuantity(orderParam.getProductQuantity());
        addOrderItem.setProductSkuId(skuProduct.getId());
        addOrderItem.setProductSkuCode(skuProduct.getSkuCode());
        addOrderItem.setProductCategoryId(product.getProductCategoryId());
        //判断商品是否都有库存
        if (product.getStock() <= 0) {
            return CommonResult.failed("库存不足，无法下单");
        }
//        if (product.getPublishStatus() == null || product.getPublishStatus() != 0) {
//            return CommonResult.failed("商品已下架，无法下单");
//        }
        addOrderItem.setPromotionAmount(new BigDecimal(0));
        addOrderItem.setCouponAmount(new BigDecimal(0));
        addOrderItem.setIntegrationAmount(new BigDecimal(0));
        addOrderItem.setGiftGrowth(0);
        orderItemList.add(addOrderItem);
        //计算order_item的实付金额
        handleRealAmount(orderItemList);
        //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额
        OmsOrder order = new OmsOrder();
        order.setMemberId(Long.valueOf(phone));
        order.setDiscountAmount(new BigDecimal(0));
        order.setTotalAmount(calcOrderPayPeriodAmount(orderParam.getPayPeriod(), orderItemList));
        order.setFreightAmount(new BigDecimal(0));
        order.setPromotionAmount(calcPromotionAmount(orderItemList));
        order.setPromotionInfo(getOrderPromotionInfo(orderItemList));
        order.setPayPeriod(orderParam.getPayPeriod());
        if (orderParam.getCouponId() == null) {
            order.setCouponAmount(new BigDecimal(0));
        } else {
            order.setCouponId(orderParam.getCouponId());
            order.setCouponAmount(calcCouponAmount(orderItemList));
        }
        order.setIntegration(0);
        order.setIntegrationAmount(new BigDecimal(0));
        order.setPayAmount(calcOrderPayPeriodAmount(orderParam.getPayPeriod(), orderItemList));
        //转化为订单信息并插入数据库
        order.setCreateTime(new Date());
        order.setMemberUsername(phone);
        //支付方式：0->未支付；1->支付宝；2->微信 3-->网银 4-->分期
        order.setPayType(orderParam.getPayType());
        //订单来源：0->PC订单；1->app订单
        order.setSourceType(1);
        //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单 6-->已拒绝
        order.setStatus(6);
        //订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(0);

        //收货人信息：姓名、电话、邮编、地址
//        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(orderParam.getMemberReceiveAddressId(), order.getMemberId());
//        order.setReceiverName(address.getName());
//        order.setReceiverPhone(address.getPhoneNumber());
//        order.setReceiverPostCode(address.getPostCode());
//        order.setReceiverProvince(address.getProvince());
//        order.setReceiverCity(address.getCity());
//        order.setReceiverRegion(address.getRegion());
//        order.setReceiverDetailAddress(address.getDetailAddress());
        //0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        //计算赠送积分
        //order.setIntegration(calcGifIntegration(orderItemList));
        //计算赠送成长值
        //order.setGrowth(calcGiftGrowth(orderItemList));
        //生成订单号
        order.setOrderSn(generateOrderSn(order));
        // TODO: 2018/9/3 bill_*,delivery_*
        order.setReceiverName(memberService.getCurrentMallAppMemberPhone());
        order.setReceiverPhone(memberService.getCurrentMallAppMemberPhone());
        //插入order表和order_item表
        orderMapper.insert(order);
        orderDao.updatePayPeriodById(orderParam.getPayPeriod(), order.getId());
        order.setPayPeriod(orderParam.getPayPeriod());
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }
        orderItemDao.insertList(orderItemList);
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("payPeriod", orderParam.getPayPeriod());
        result.put("orderItemList", orderItemList);
        //下单成功后调用全民钱包的购物借款接口
        JSONObject qmShoppingParams = new JSONObject();
        qmShoppingParams.put("commitTime", getCurrentDate());
        qmShoppingParams.put("mobile", memberService.getCurrentMallAppMemberPhone());
        qmShoppingParams.put("orderNo", order.getOrderSn());
        qmShoppingParams.put("term", orderParam.getPayPeriod());
        qmShoppingParams.put("totalPrice", order.getTotalAmount());
        Response response = OkHttpUtil.post(qmWalletAppConfig.getOrderReceivUrl(),
                JSON.toJSONString(qmShoppingParams));
        if (response.isSuccessful()) {
            QmOrderResponse qmOrderResponse = JSON.parseObject(response.body().string(), QmOrderResponse.class);
            if (qmOrderResponse.getCode() == 0) {
                return CommonResult.success(result, "下单成功");
            } else {
                log.error("##################### 全民钱包购物订单同步异常: {}", qmOrderResponse.getMessage());
                return CommonResult.failed("下单失败");
            }
        } else {
            log.error("##################### 全民钱包购物订单同步异常: {}", response.message());
            return CommonResult.failed("下单失败");
        }
    }

    @Override
    public CommonResult paySuccess(Long orderId) {
        return null;
    }

    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        return sb.toString();
    }




    private void handleRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem orderItem : orderItemList) {
            //原价-促销优惠-优惠券抵扣-积分抵扣
            BigDecimal realAmount = orderItem.getProductPrice()
                    .subtract(orderItem.getPromotionAmount())
                    .subtract(orderItem.getCouponAmount())
                    .subtract(orderItem.getIntegrationAmount());
            orderItem.setRealAmount(realAmount);
        }
    }

    /**
     * 获取订单促销信息串
     */
    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder promotionNames = new StringBuilder();
        for (OmsOrderItem orderItem : orderItemList) {
            promotionNames.append(orderItem.getPromotionName());
            promotionNames.append(",");
        }

        String orderPromotionInfo = promotionNames.toString();
        if (orderPromotionInfo.endsWith(",")) {
            orderPromotionInfo = orderPromotionInfo.substring(0, orderPromotionInfo.length() - 1);
        }
        return orderPromotionInfo;
    }


    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal integrationAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getIntegrationAmount() != null) {
                integrationAmount = integrationAmount.add(orderItem.getIntegrationAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return integrationAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal couponAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getCouponAmount() != null) {
                couponAmount = couponAmount.add(orderItem.getCouponAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return couponAmount;
    }

    /**
     * 计算订单活动优惠
     */
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal promotionAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getPromotionAmount() != null) {
                promotionAmount = promotionAmount.add(orderItem.getPromotionAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return promotionAmount;
    }


    /**
     * 统计订单的总支付费用
     * @param payPeriods 分期数
     * @param orderItemList 可用优惠券的下单商品商品
     */
    private BigDecimal calcOrderPayPeriodAmount(int payPeriods, List<OmsOrderItem> orderItemList) {
        BigDecimal totalAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            BigDecimal productPrice = orderItem.getProductPrice();
            totalAmount = totalAmount.add(calculateSumPerPeriodPrice(payPeriods, productPrice));
        }
        return totalAmount;
    }



    /**
     * 统计该件商品所有的分期总金额
     * @param payPeriods
     * @return
     */
    public BigDecimal calculateSumPerPeriodPrice(int payPeriods, BigDecimal price){
        BigDecimal sumPrice = new BigDecimal(0);
        if(payPeriods >=0 && MathUtil.gt(price,BigDecimal.ZERO)){
            //手续费
            BigDecimal charge = MathUtil.mul(price, new BigDecimal(0.01));
            sumPrice = MathUtil.add(price, MathUtil.mul(charge, new BigDecimal(payPeriods))).setScale(2, BigDecimal.ROUND_DOWN);
        }
        return  sumPrice;
    }

    /**
     * 获取系统时间并返回时间格式
     */
    public static String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(new Date());
        return dateStr;
    }

}
