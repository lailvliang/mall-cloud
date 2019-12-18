package com.macro.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {

    private Long id;
    /**属性名称*/
    private String name;
    /**属性值*/
    private String value;
    /**排序*/
    private Integer sort;
}
