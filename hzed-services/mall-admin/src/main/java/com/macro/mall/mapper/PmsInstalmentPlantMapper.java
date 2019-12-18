package com.macro.mall.mapper;

import com.macro.mall.model.PmsInstalmentPlant;
import org.springframework.stereotype.Repository;

/**
* Created by Mybatis Generator on 2019/11/13
 * @author Administrator
*/
@Repository
public interface PmsInstalmentPlantMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PmsInstalmentPlant record);

    int insertSelective(PmsInstalmentPlant record);

    PmsInstalmentPlant selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsInstalmentPlant record);

    int updateByPrimaryKey(PmsInstalmentPlant record);
}