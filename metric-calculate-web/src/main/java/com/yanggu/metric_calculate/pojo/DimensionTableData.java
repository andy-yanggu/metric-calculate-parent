package com.yanggu.metric_calculate.pojo;

import com.yanggu.metric_calculate.core2.table.Table;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class DimensionTableData {

    private LinkedHashMap<String, Object> dimensionMap;

    private Table table;

}
