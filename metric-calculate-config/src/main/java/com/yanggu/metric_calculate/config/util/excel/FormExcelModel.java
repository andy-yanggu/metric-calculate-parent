package com.yanggu.metric_calculate.config.util.excel;

import lombok.Data;

import java.util.List;

@Data
public class FormExcelModel {

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 列名
     */
    private List<String> title;

    /**
     * 数据
     */
    private List<Object> data;

}
