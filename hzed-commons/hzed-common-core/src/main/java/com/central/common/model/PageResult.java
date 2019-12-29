package com.central.common.model;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页实体类
 *
 * @author hzed
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -275582248840137389L;
    /**
     * 总数
     */
    private Long count;
    /**
     * 是否成功：0 成功、1 失败
     */
    private int code;
    /**
     * 当前页结果集
     */
    private List<T> data;

    private int totalPage;


    public static <T> PageResult<T> restPage(List<T> list) {
        PageResult<T> result = new PageResult<T>();
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        result.setTotalPage(pageInfo.getPages());
        result.setCount(pageInfo.getTotal());
        result.setData(pageInfo.getList());
        return result;
    }
}
