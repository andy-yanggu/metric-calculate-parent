package com.yanggu.metric_calculate.config.test;

import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionMapper;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
class TestMapper {

    @Autowired
    private AggregateFunctionMapper aggregateFunctionMapper;

    //@Test
    void test1() {
        AggregateFunction aggregateFunction = new AggregateFunction();
        aggregateFunction.setName("SUM");
        aggregateFunction.setDisplayName("求和");
        aggregateFunction.setUserId(1);
        int insert = aggregateFunctionMapper.insertSelective(aggregateFunction);
        System.out.println(insert);
    }

}
