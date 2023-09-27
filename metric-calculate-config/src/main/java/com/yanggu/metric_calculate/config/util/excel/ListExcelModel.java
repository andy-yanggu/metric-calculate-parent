package com.yanggu.metric_calculate.config.util.excel;

import lombok.Data;

import java.util.List;

@Data
public class ListExcelModel {

    private List<String> title;

    private List<List<Object>> data;

    private String sheetName;

}
