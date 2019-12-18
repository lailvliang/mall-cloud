package com.macro.mall.service.impl;

import com.macro.mall.service.PmsProductService;
import com.macro.mall.service.PmsSkuStockService;
import com.macro.mall.dao.PmsSkuStockDao;
import com.macro.mall.dto.PmsProductParam;
import com.macro.mall.mapper.PmsProductMapper;
import com.macro.mall.mapper.PmsSkuStockMapper;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsSkuStock;
import com.macro.mall.model.PmsSkuStockExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品sku库存管理Service实现类
 * Created by macro on 2018/4/27.
 */
@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    @Autowired
    private PmsSkuStockMapper skuStockMapper;
    @Autowired
    private PmsSkuStockDao skuStockDao;

    @Autowired
    private PmsProductService pmsProductService;
    @Autowired
    private PmsProductMapper productMapper;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        PmsSkuStockExample example = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = example.createCriteria().andProductIdEqualTo(pid);
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andSkuCodeLike("%" + keyword + "%");
        }
        return skuStockMapper.selectByExample(example);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        //更新关联的产品信息（库存和价格）
        updRelationProductInfo(pid, skuStockList);
        return skuStockDao.replaceList(skuStockList);
    }

    /**
     * 更新关联的产品信息（库存、价格）
     * @param pid
     * @param skuStockList
     */
    private void updRelationProductInfo(Long pid, List<PmsSkuStock> skuStockList) {
        PmsProductParam productParam = pmsProductService.getUpdateInfo(pid);
        //即将更新的库存信息
        productParam.setSkuStockList(skuStockList);
        PmsProduct product = pmsProductService.handlePmsProduct(productParam ,pid);
        //更新产品
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public PmsSkuStock find(String skuCode, Long productId) {
        List<PmsSkuStock> list = skuStockDao .find(skuCode ,productId);
        return CollectionUtils.isEmpty(list)?null : list.get(0);
    }
}
