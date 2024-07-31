package com.yanggu.metric_calculate.config.base.domain.vo;


import com.yanggu.metric_calculate.config.base.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页数据封装
 *
 * @param <T> 必须是VO
 */
@Data
@NoArgsConstructor
public class PageVO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3563028771440196184L;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    private Long pageNum;

    /**
     * 每页显示条数
     */
    @Schema(description = "每页显示条数")
    private Long pageSize;

    /**
     * 分页数据
     */
    @Schema(description = "分页数据")
    private List<T> records;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private Long total;

    /**
     * 将其余VO分页数据转换成PageVO分页数据
     */
    public PageVO(PageQuery<T> pageQuery) {
        this.pageNum = pageQuery.getPageNum();
        this.pageSize = pageQuery.getPageSize();
        this.records = pageQuery.getRecords();
        this.total = pageQuery.getTotalPage();
    }

    /**
     * 转成成分页数据通过全量参数
     *
     * @param records 已经分页后的list
     */
    public PageVO(Long pageNum, Long pageSize, Long total, List<T> records) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.records = records;
    }

    /**
     * 转换成分页数据，手动分页
     *
     * @param records 分页前的数据
     */
    public PageVO(Long pageNum, Long pageSize, List<T> records) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = (long) records.size();
        this.records = records.stream()
                .skip((pageNum - 1) * pageSize)
                .limit(pageSize)
                .toList();
    }

}
