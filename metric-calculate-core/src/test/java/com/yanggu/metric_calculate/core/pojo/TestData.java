package com.yanggu.metric_calculate.core.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TestData {

    private Input input;

    private Map<List<String>, Double> output;

    @Data
    public static class Input {

        private String accountNoIn;

        private Double amount;
    }

}
