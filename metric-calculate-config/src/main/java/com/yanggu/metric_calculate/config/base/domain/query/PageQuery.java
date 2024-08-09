package com.yanggu.metric_calculate.config.base.domain.query;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

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
    @Schema(description = "当前页码", defaultValue = "1")
    private long pageNum;

    /**
     * 每页大小
     */
    @NotNull(message = "pageSize为空")
    @Schema(description = "每页大小", defaultValue = "10")
    private long pageSize = FlexGlobalConfig.getDefaultConfig().getDefaultPageSize();

    /**
     * 总页数。
     */
    @Hidden
    private long totalPage = INIT_VALUE;

    /**
     * 总数据数量。
     */
    @Hidden
    private long totalRow = INIT_VALUE;

    /**
     * 是否优化分页查询 COUNT 语句。
     */
    @Hidden
    private boolean optimizeCountQuery = true;

    /**
     * 当前页数据。
     */
    @Hidden
    private List<T> records = Collections.emptyList();

}
