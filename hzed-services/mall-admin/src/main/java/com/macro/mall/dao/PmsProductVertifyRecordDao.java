package com.macro.mall.dao;

import com.macro.mall.model.PmsProductVertifyRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品审核日志自定义dao
 * Created by macro on 2018/4/27.
 */
@Repository
public interface PmsProductVertifyRecordDao {
    int insertList(@Param("list") List<PmsProductVertifyRecord> list);
}
