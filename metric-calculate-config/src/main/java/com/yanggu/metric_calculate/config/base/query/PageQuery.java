package com.yanggu.metric_calculate.config.base.query;

import com.mybatisflex.core.paginate.Page;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 分页查询参数基类，分页查询入参和返回基类
 *
 * @param <T> 必须是Entity或者是VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageQuery<T> extends Page<T> {

    @Serial
    private static final long serialVersionUID = 2101820826443930585L;

    /**
     * 当前页码
     */
    @NotNull(message = "pageNum为空")
    @Min(value = 1L, message = "pageNum必须大于等于1")
    private long pageNum;

    /**
     * 每页大小
     */
    @NotNull(message = "pageSize为空")
    @Min(value = 1L, message = "分页大小不能小于1")
    private long pageSize;

}
