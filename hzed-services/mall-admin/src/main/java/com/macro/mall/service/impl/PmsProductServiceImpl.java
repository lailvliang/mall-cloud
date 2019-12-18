package com.macro.mall.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.central.user.dao.*;
import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.service.PmsProductService;
import com.macro.mall.user.dao.*;
import com.github.pagehelper.PageHelper;
import com.macro.mall.common.CommonResult;
import com.macro.mall.common.IdUtil;
import com.macro.mall.dao.*;
import com.macro.mall.domain.MallHomeDetail;
import com.macro.mall.domain.MallHomeProduct;
import com.macro.mall.dto.PmsProductParam;
import com.macro.mall.dto.PortalHomeProductDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商品管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductServiceImpl implements PmsProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsMemberPriceDao memberPriceDao;
    @Autowired
    private PmsMemberPriceMapper memberPriceMapper;
    @Autowired
    private PmsProductLadderDao productLadderDao;
    @Autowired
    private PmsProductLadderMapper productLadderMapper;
    @Autowired
    private PmsProductFullReductionDao productFullReductionDao;
    @Autowired
    private PmsProductFullReductionMapper productFullReductionMapper;
    @Autowired
    private PmsSkuStockDao skuStockDao;
    @Autowired
    private PmsSkuStockMapper skuStockMapper;
    @Autowired
    private PmsProductAttributeValueDao productAttributeValueDao;
    @Autowired
    private PmsProductAttributeValueMapper productAttributeValueMapper;
    @Autowired
    private CmsSubjectProductRelationDao subjectProductRelationDao;
    @Autowired
    private CmsSubjectProductRelationMapper subjectProductRelationMapper;
    @Autowired
    private CmsPrefrenceAreaProductRelationDao prefrenceAreaProductRelationDao;
    @Autowired
    private CmsPrefrenceAreaProductRelationMapper prefrenceAreaProductRelationMapper;
    @Autowired
    private PmsProductDao productDao;
    @Autowired
    private PmsProductVertifyRecordDao productVertifyRecordDao;
    @Autowired
    private PmsInstalmentPlantMapper pmsInstalmentPlantMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int create(PmsProductParam productParam){
        int count;
        //创建商品
        PmsProduct product = handlePmsProduct(productParam ,null);
        productMapper.insertSelective(product);
        //根据促销类型设置价格：、阶梯价格、满减价格
        Long productId = product.getId();
        //商品分期计划
        savePmsInstalmentPlant(productParam.getPmsInstalmentPlant(),productId);
        //会员价格
        relateAndInsertList(memberPriceDao, productParam.getMemberPriceList(), productId);
        //阶梯价格
        relateAndInsertList(productLadderDao, productParam.getProductLadderList(), productId);
        //满减价格
        relateAndInsertList(productFullReductionDao, productParam.getProductFullReductionList(), productId);
        //处理sku的编码
        handleSkuStockCode(productParam.getSkuStockList(),productId);
        //添加sku库存信息
        relateAndInsertList(skuStockDao, productParam.getSkuStockList(), productId);
        //添加商品参数,添加自定义商品规格
        relateAndInsertList(productAttributeValueDao, productParam.getProductAttributeValueList(), productId);
        //关联专题
        relateAndInsertList(subjectProductRelationDao, productParam.getSubjectProductRelationList(), productId);
        //关联优选
        relateAndInsertList(prefrenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), productId);
        count = 1;
        return count;
    }

    private void savePmsInstalmentPlant(PmsInstalmentPlant pmsInstalmentPlant, Long productId){
        if(Objects.nonNull(pmsInstalmentPlant)){
            pmsInstalmentPlant.setProductId(productId);
            pmsInstalmentPlantMapper.insert(pmsInstalmentPlant);
        }
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {

        if (CollectionUtils.isEmpty(skuStockList)) {
            return;
        }

        if(CollectionUtils.isEmpty(skuStockList)){
            return;
        }
        for (int i=0; i<skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            if(StringUtils.isEmpty(skuStock.getSkuCode())){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                //日期
                sb.append(sdf.format(new Date()));
                //四位商品id
                sb.append(String.format("%04d", productId));
                //三位索引id
                sb.append(String.format("%03d", i+1));
                skuStock.setSkuCode(sb.toString());
            }
            Object dynamicPros = skuStock.getDynamicPros();
            if(Objects.nonNull(dynamicPros)){
                if(dynamicPros instanceof List){
                    String json = JSONUtil.toJsonStr(dynamicPros);
                    skuStock.setDynamicPros(json);
                }
                if(dynamicPros instanceof JSONObject){
                    String json = JSONUtil.toJsonStr(dynamicPros);
                    skuStock.setDynamicPros(json);
                }
            }
        }
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return productDao.getUpdateInfo(id);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(Long id, PmsProductParam productParam) {
        int count;
        //更新商品信息
        PmsProduct product = handlePmsProduct(productParam ,id);
        //更新产品
        productMapper.updateByPrimaryKeySelective(product);
        //更新产品分期
        pmsInstalmentPlantMapper.updateByPrimaryKeySelective(productParam.getPmsInstalmentPlant());
        //会员价格
        PmsMemberPriceExample pmsMemberPriceExample = new PmsMemberPriceExample();
        pmsMemberPriceExample.createCriteria().andProductIdEqualTo(id);
        memberPriceMapper.deleteByExample(pmsMemberPriceExample);
        relateAndInsertList(memberPriceDao, productParam.getMemberPriceList(), id);
        //阶梯价格
        PmsProductLadderExample ladderExample = new PmsProductLadderExample();
        ladderExample.createCriteria().andProductIdEqualTo(id);
        productLadderMapper.deleteByExample(ladderExample);
        relateAndInsertList(productLadderDao, productParam.getProductLadderList(), id);
        //满减价格
        PmsProductFullReductionExample fullReductionExample = new PmsProductFullReductionExample();
        fullReductionExample.createCriteria().andProductIdEqualTo(id);
        productFullReductionMapper.deleteByExample(fullReductionExample);
        relateAndInsertList(productFullReductionDao, productParam.getProductFullReductionList(), id);
        //修改sku库存信息
        PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
        skuStockExample.createCriteria().andProductIdEqualTo(id);
        skuStockMapper.deleteByExample(skuStockExample);
        handleSkuStockCode(productParam.getSkuStockList(),id);
        relateAndInsertList(skuStockDao, productParam.getSkuStockList(), id);
        //修改商品参数,添加自定义商品规格
        PmsProductAttributeValueExample productAttributeValueExample = new PmsProductAttributeValueExample();
        productAttributeValueExample.createCriteria().andProductIdEqualTo(id);
        productAttributeValueMapper.deleteByExample(productAttributeValueExample);
        relateAndInsertList(productAttributeValueDao, productParam.getProductAttributeValueList(), id);
        //关联专题
        CmsSubjectProductRelationExample subjectProductRelationExample = new CmsSubjectProductRelationExample();
        subjectProductRelationExample.createCriteria().andProductIdEqualTo(id);
        subjectProductRelationMapper.deleteByExample(subjectProductRelationExample);
        relateAndInsertList(subjectProductRelationDao, productParam.getSubjectProductRelationList(), id);
        //关联优选
        CmsPrefrenceAreaProductRelationExample prefrenceAreaExample = new CmsPrefrenceAreaProductRelationExample();
        prefrenceAreaExample.createCriteria().andProductIdEqualTo(id);
        prefrenceAreaProductRelationMapper.deleteByExample(prefrenceAreaExample);
        relateAndInsertList(prefrenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), id);
        count = 1;
        return count;
    }

    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if (productQueryParam.getPublishStatus() != null) {
            criteria.andPublishStatusEqualTo(productQueryParam.getPublishStatus());
        }
        if (productQueryParam.getVerifyStatus() != null) {
            criteria.andVerifyStatusEqualTo(productQueryParam.getVerifyStatus());
        }
        if (!StringUtils.isEmpty(productQueryParam.getKeyword())) {
            criteria.andNameLike("%" + productQueryParam.getKeyword() + "%");
        }
        if (!StringUtils.isEmpty(productQueryParam.getProductSn())) {
            criteria.andProductSnEqualTo(productQueryParam.getProductSn());
        }
        if (productQueryParam.getBrandId() != null) {
            criteria.andBrandIdEqualTo(productQueryParam.getBrandId());
        }
        if (productQueryParam.getProductCategoryId() != null) {
            criteria.andProductCategoryIdEqualTo(productQueryParam.getProductCategoryId());
        }
        return productMapper.selectByExample(productExample);
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        PmsProduct product = new PmsProduct();
        product.setVerifyStatus(verifyStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        List<PmsProductVertifyRecord> list = new ArrayList<>();
        int count = productMapper.updateByExampleSelective(product, example);
        //修改完审核状态后插入审核记录
        for (Long id : ids) {
            PmsProductVertifyRecord record = new PmsProductVertifyRecord();
            record.setProductId(id);
            record.setCreateTime(new Date());
            record.setDetail(detail);
            record.setStatus(verifyStatus);
            record.setVertifyMan("test");
            list.add(record);
        }
        productVertifyRecordDao.insertList(list);
        return count;
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        PmsProduct record = new PmsProduct();
        record.setPublishStatus(publishStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        PmsProduct record = new PmsProduct();
        record.setRecommandStatus(recommendStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setNewStatus(newStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        PmsProduct record = new PmsProduct();
        record.setDeleteStatus(deleteStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(record, example);
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if(!StringUtils.isEmpty(keyword)){
            criteria.andNameLike("%" + keyword + "%");
            productExample.or().andDeleteStatusEqualTo(0).andProductSnLike("%" + keyword + "%");
        }
        return productMapper.selectByExample(productExample);
    }

    @Override
    public List<PortalHomeProductDetail> getHotProductListTopThree() {
        List<PortalHomeProductDetail> details = productDao.getHotProductListTopThree();
        if (details != null && details.size() > 0) {
            details.forEach((product) -> {
                try {
                    //将数据库查询出的原始状态转换成前端需要的数据类型
                    product.transformTypes().transformSkuProducts();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return details;
    }

    @Override
    public List<PortalHomeProductDetail> getCategoryProductList() {
        return productDao.getCategoryProductList();
    }

    @Override
    public CommonResult<MallHomeDetail> getMallHomeContent() {
        MallHomeDetail mallHomeDetail = new MallHomeDetail();
        //商城首页展示的数据
        List<MallHomeProduct> data = new ArrayList<>();
        //一级分类
        Map<Integer, String> parentCategoryMap = new HashMap<>(3);
        //二级分类
        Map<Integer, String> subCategoryMap = new HashMap<>(20);

        Map<Integer, Map<String, List<PortalHomeProductDetail>>> categoryProductMap = new HashMap<>(5);
        List<PortalHomeProductDetail> promotionProducts = getCategoryProductList();
        if (promotionProducts != null) {
            for (PortalHomeProductDetail product : promotionProducts) {
                try {
                    //将数据库查询出的原始状态转换成前端需要的数据类型
                    product.transformTypes().transformSkuProducts();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 二级分类的key 为一级分类id+@+二级分类id
                String subKey = product.getPaCategoryId() + "@" + product.getChCategoryId();
                if (categoryProductMap.containsKey(product.getPaCategoryId())) {
                    Map<String, List<PortalHomeProductDetail>> subMap = categoryProductMap.get(product.getPaCategoryId());
                    if (subMap.containsKey(subKey)) {
                        subMap.get(subKey).add(product);
                    } else {
                        List<PortalHomeProductDetail> subProducts = new ArrayList<>();
                        subProducts.add(product);
                        subMap.put(subKey, subProducts);
                    }
                } else {
                    Map<String, List<PortalHomeProductDetail>> subMap = new HashMap<>(5);
                    List<PortalHomeProductDetail> subProducts = new ArrayList<>();
                    subProducts.add(product);
                    subMap.put(subKey, subProducts);
                    categoryProductMap.put(product.getPaCategoryId(), subMap);
                }
                parentCategoryMap.put(product.getPaCategoryId(), product.getPaCategoryName());
                subCategoryMap.put(product.getChCategoryId(), product.getChCategoryName());
            }
        }
        //格式化app前端展示页面数据
        categoryProductMap.forEach((key, value) -> {
            MallHomeProduct mallHomeProduct = new MallHomeProduct();
            mallHomeProduct.setCategoryId(key);
            mallHomeProduct.setCategoryName(parentCategoryMap.get(key));
            List<MallHomeProduct.SubGroupProduct> products = new ArrayList<>();
            //循环二级分类商品列表数据
            value.forEach((subKey, subValue) -> {
                MallHomeProduct.SubGroupProduct subGroupProduct = new MallHomeProduct.SubGroupProduct();
                Integer subGroupKey = Integer.valueOf(subKey.split("@")[1]);
                subGroupProduct.setCategoryId(subGroupKey);
                subGroupProduct.setCategoryName(subCategoryMap.get(subGroupKey));
                subGroupProduct.setProducts(subValue);
                products.add(subGroupProduct);
            });
            mallHomeProduct.setProducts(products);
            data.add(mallHomeProduct);
        });
        mallHomeDetail.setHotProducts(getHotProductListTopThree());
        mallHomeDetail.setCategoryProductMap(data);
        return  CommonResult.success(mallHomeDetail);
    }

    /**
     * @deprecated 旧版创建
     */
    public int createOld(PmsProductParam productParam) {
        int count;
        //创建商品
        PmsProduct product = productParam;
        product.setId(null);
        productMapper.insertSelective(product);
        //根据促销类型设置价格：、阶梯价格、满减价格
        Long productId = product.getId();
        //会员价格
        List<PmsMemberPrice> memberPriceList = productParam.getMemberPriceList();
        if (!CollectionUtils.isEmpty(memberPriceList)) {
            for (PmsMemberPrice pmsMemberPrice : memberPriceList) {
                pmsMemberPrice.setId(null);
                pmsMemberPrice.setProductId(productId);
            }
            memberPriceDao.insertList(memberPriceList);
        }
        //阶梯价格
        List<PmsProductLadder> productLadderList = productParam.getProductLadderList();
        if (!CollectionUtils.isEmpty(productLadderList)) {
            for (PmsProductLadder productLadder : productLadderList) {
                productLadder.setId(null);
                productLadder.setProductId(productId);
            }
            productLadderDao.insertList(productLadderList);
        }
        //满减价格
        List<PmsProductFullReduction> productFullReductionList = productParam.getProductFullReductionList();
        if (!CollectionUtils.isEmpty(productFullReductionList)) {
            for (PmsProductFullReduction productFullReduction : productFullReductionList) {
                productFullReduction.setId(null);
                productFullReduction.setProductId(productId);
            }
            productFullReductionDao.insertList(productFullReductionList);
        }
        //添加sku库存信息
        List<PmsSkuStock> skuStockList = productParam.getSkuStockList();
        if (!CollectionUtils.isEmpty(skuStockList)) {
            for (PmsSkuStock skuStock : skuStockList) {
                skuStock.setId(null);
                skuStock.setProductId(productId);
            }
            skuStockDao.insertList(skuStockList);
        }
        //添加商品参数,添加自定义商品规格
        List<PmsProductAttributeValue> productAttributeValueList = productParam.getProductAttributeValueList();
        if (!CollectionUtils.isEmpty(productAttributeValueList)) {
            for (PmsProductAttributeValue productAttributeValue : productAttributeValueList) {
                productAttributeValue.setId(null);
                productAttributeValue.setProductId(productId);
            }
            productAttributeValueDao.insertList(productAttributeValueList);
        }
        //关联专题
        relateAndInsertList(subjectProductRelationDao, productParam.getSubjectProductRelationList(), productId);
        //关联优选
        relateAndInsertList(prefrenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), productId);
        count = 1;
        return count;
    }

    /**
     * 建立和插入关系表操作
     *
     * @param dao       可以操作的dao
     * @param dataList  要插入的数据
     * @param productId 建立关系的id
     */
    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item , IdUtil.nextId());
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = dao.getClass().getMethod("insertList", List.class);
            insertList.invoke(dao, dataList);
        } catch (Exception e) {
            LOGGER.warn("创建产品出错:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 保存入库前，设置相关必要的字段值
     * @param pmsProductParam
     * @return
     */
    @Override
    public PmsProduct handlePmsProduct(PmsProductParam pmsProductParam ,Long productId){
        PmsProduct product = pmsProductParam;
        //如果有指定id，使用指定id(更新场景)；如果没有，生成自定义id（新增场景）
        if(Objects.nonNull(productId)){
            product.setId(productId);
        }else{
            product.setId(IdUtil.nextId());
        }

        //设置商品库存值、价格
        List<PmsSkuStock> skuStockList = pmsProductParam.getSkuStockList();
        if( !CollectionUtils.isEmpty(skuStockList) ){
            //设置商品库存值，取sku记录中最小的值
            Optional<Integer> lowStockOpt = skuStockList.stream().filter(e -> e.getStock() != null).map(PmsSkuStock::getStock).distinct().min((e1, e2) -> e1.compareTo(e2));
            if(Objects.nonNull(lowStockOpt) && lowStockOpt.isPresent()){
                product.setStock(lowStockOpt.get());
            }
            //设置商品价格，取sku记录中最小的价格
            Optional<BigDecimal> priceOpt = skuStockList.stream().filter(e -> e.getPrice() != null).map(PmsSkuStock::getPrice).distinct().min((e1, e2) -> e1.compareTo(e2));
            if(Objects.nonNull(priceOpt) && priceOpt.isPresent()){
                product.setPrice(priceOpt.get());
            }

        }
        return product;
    }
}
