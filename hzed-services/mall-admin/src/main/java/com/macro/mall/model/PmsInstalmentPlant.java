package com.macro.mall.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Created by Mybatis Generator on 2019/11/13
 * @author
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmsInstalmentPlant extends AbstractEntity {

    private Long productId;

    @ApiModelProperty(value = "付款分期期数： 3,6,12或 6,12,24 中间逗号分隔")
    private String payPeriods;

    @ApiModelProperty(value = "是否有效 1-有效 0-无效")
    private Byte valid;

    private static final long serialVersionUID = 1L;

}