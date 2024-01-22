package com.yanggu.metric_calculate.config.base.vo;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页数据封装
 *
 * @param <T> 必须是VO
 */
@Data
public class PageVO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3563028771440196184L;

    /**
     * 当前页码
     */
    private Long pageNum;

    /**
     * 每页显示条数
     */
    private Long pageSize;

    /**
     * 分页数据
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

}
