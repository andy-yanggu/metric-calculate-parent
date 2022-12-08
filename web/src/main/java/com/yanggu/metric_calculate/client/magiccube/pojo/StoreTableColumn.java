package com.yanggu.metric_calculate.client.magiccube.pojo;

import com.yanggu.metric_calculate.client.magiccube.enums.BasicType;
import com.yanggu.metric_calculate.client.magiccube.enums.StoreColumnTypeEnum;
import lombok.Data;

/**
 * 存储宽表字段
 */
@Data
public class StoreTableColumn {

    /**
     * 存储宽表字段id
     */
    private Long id;

    /**
     * 字段名称.
     */
    private String columnName;

    /**
     * 如果字段是维度字段, 这里维度名称
     */
    private String dimensionName;

    /**
     * 索引.
     */
    private Integer storeColumnIndex;

    /**
     * 字段类型(Long、String、boolean、BigDecimal等).
     */
    private BasicType columnType;

    /**
     * 字段存储类型（是否维度 时间 度量）.
     */
    private StoreColumnTypeEnum storeColumnType;

}
