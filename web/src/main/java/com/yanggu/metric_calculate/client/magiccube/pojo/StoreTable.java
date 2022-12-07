package com.yanggu.metric_calculate.client.magiccube.pojo;

import com.yanggu.metric_calculate.client.magiccube.enums.BasicType;
import lombok.Data;

import java.util.List;

/**
 * 指标存储宽表
 */
@Data
public class StoreTable {

    /**
     * 存储宽表
     */
    private String storeTable;

    /**
     * 存储时间格式
     */
    private String storeTimeFormat;

    /**
     * 存储字段名
     */
    private String storeColumn;

    /**
     * 字段类型(Long、String、boolean、BigDecimal等).
     */
    private BasicType columnType;

    /**
     * 存储类型, HBASE、KAFKA、ES、MYSQL等
     */
    private String storeType;

    /**
     * 指标存储宽表字段、维度字段和指标字段
     */
    private List<StoreTableColumn> storeColumnDtoList;

}
