package com.yanggu.metric_calculate.config.test;

import com.yanggu.metric_calculate.config.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMapper {

    @Autowired
    private AggregateFunctionMapper aggregateFunctionMapper;

    //@Test
    public void test1() {
        AggregateFunction aggregateFunction = new AggregateFunction();
        aggregateFunction.setName("COUNT");
        aggregateFunction.setUserId(1);
        int insert = aggregateFunctionMapper.insert(aggregateFunction);
        System.out.println(insert);
    }

}
