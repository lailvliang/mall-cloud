package com.macro.mall.service;

import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.common.CommonResult;
import com.macro.mall.domain.MallHomeDetail;
import com.macro.mall.dto.PmsProductParam;
import com.macro.mall.dto.PortalHomeProductDetail;
import com.macro.mall.model.PmsProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品管理Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductService {

    /**
     * 创建商品
     * @param productParam
     * @return
     * @throws Exception
     */
//    @Transactional(rollbackFor = Exception.class,isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    int create(PmsProductParam productParam) throws Exception;

    /**
     * 根据商品编号获取更新信息
     */
    PmsProductResult getUpdateInfo(Long id);

    /**
     * 更新商品
     */
    int update(Long id, PmsProductParam productParam);

    /**
     * 分页查询商品
     */
    List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量修改审核状态
     * @param ids 产品id
     * @param verifyStatus 审核状态
     * @param detail 审核详情
     */
    @Transactional
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 批量修改商品上架状态
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量修改商品推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量修改新品状态
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量删除商品
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 根据商品名称或者货号模糊查询
     */
    List<PmsProduct> list(String keyword);

    /**
     * 获取上架促销热门推荐商品top 3
     * @return
     */
    List<PortalHomeProductDetail> getHotProductListTopThree();

    /**
     * 获取首页促销商品分类展示列表
     * 展示分类：获取后台“商品分类”中“排序”前3个一级分类,再获取各个一级分类下的“排序”前3的二级分类，在获取各个二级分类下的6个随机商品（总商品数量：3*3*6=54个）
     * 分类标题展示：二级标题。每个二级标题展示顺序随机，如图“数码类”和“美妆类”。
     * 商品展示：获取各个二级分类下的6个随机商品个，如图6个商品。
     * @return
     */
    List<PortalHomeProductDetail> getCategoryProductList();

    /**
     * 获取商城首页内容
     * @return 首页内容详情
     */
    CommonResult<MallHomeDetail> getMallHomeContent();

    /**
     * 设置产品信息
     * @param pmsProductParam 产品
     * @param productId 产品id
     * @return
     */
    public PmsProduct handlePmsProduct(PmsProductParam pmsProductParam, Long productId);
}
