package com.yanggu.metric_calculate.core.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class TestData {

    private Input input;

    private Map<List<String>, BigDecimal> output;

    @Data
    public static class Input {

        private String accountNoIn;

        private BigDecimal amount;
    }

}
