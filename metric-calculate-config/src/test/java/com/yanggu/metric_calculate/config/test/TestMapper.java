package com.yanggu.metric_calculate.config.test;

import com.yanggu.metric_calculate.config.mapper.AggregateFunctionMapper;
import com.yanggu.metric_calculate.config.domain.entity.AggregateFunctionEntity;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
class TestMapper {

    @Autowired
    private AggregateFunctionMapper aggregateFunctionMapper;

    //@Test
    void test1() {
        AggregateFunctionEntity aggregateFunction = new AggregateFunctionEntity();
        aggregateFunction.setName("SUM");
        aggregateFunction.setDisplayName("求和");
        aggregateFunction.setUserId(1);
        int insert = aggregateFunctionMapper.insertSelective(aggregateFunction);
        System.out.println(insert);
    }

}
