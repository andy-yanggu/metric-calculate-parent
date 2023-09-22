package com.yanggu.metric_calculate.config.util.excel;

import lombok.Data;

import java.util.List;

@Data
public class ImportResult<T> {

    private Boolean success;

    private List<T> data;

    private byte[] file;
}
