package com.yanggu.metric_calculate.config.test;

import com.mybatisflex.core.query.QueryWrapper;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionMapper;
import com.yanggu.metric_calculate.config.mapper.DeriveMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.yanggu.metric_calculate.config.enums.WindowTypeEnum.TUMBLING_TIME_WINDOW;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AggregateFunctionParamTableDef.AGGREGATE_FUNCTION_PARAM;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AggregateFunctionTableDef.AGGREGATE_FUNCTION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveAggregateFunctionParamRelationTableDef.DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveModelTimeColumnRelationTableDef.DERIVE_MODEL_TIME_COLUMN_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveTableDef.DERIVE;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveWindowParamRelationTableDef.DERIVE_WINDOW_PARAM_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelColumnTableDef.MODEL_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTableDef.MODEL;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTimeColumnTableDef.MODEL_TIME_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.WindowParamTableDef.WINDOW_PARAM;

@SpringBootTest
class TestMapper {

    @Autowired
    private AggregateFunctionMapper aggregateFunctionMapper;

    @Autowired
    private DeriveMapper deriveMapper;

    //@Test
    void test1() {
        AggregateFunction aggregateFunction = new AggregateFunction();
        aggregateFunction.setName("SUM");
        aggregateFunction.setDisplayName("求和");
        aggregateFunction.setUserId(1);
        int insert = aggregateFunctionMapper.insertSelective(aggregateFunction);
        System.out.println(insert);
    }

    @Test
    void test2() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(DERIVE.DEFAULT_COLUMNS)
                .from(DERIVE)
                //数据明细宽表
                .innerJoin(MODEL).on(MODEL.ID.eq(DERIVE.MODEL_ID))
                //时间字段
                .innerJoin(DERIVE_MODEL_TIME_COLUMN_RELATION).on(DERIVE_MODEL_TIME_COLUMN_RELATION.DERIVE_ID.eq(DERIVE.ID))
                .innerJoin(MODEL_TIME_COLUMN).on(MODEL_TIME_COLUMN.ID.eq(DERIVE_MODEL_TIME_COLUMN_RELATION.MODEL_TIME_COLUMN_ID))
                .innerJoin(MODEL_COLUMN).as("time_column").on(MODEL_COLUMN.ID.eq(MODEL_TIME_COLUMN.MODEL_COLUMN_ID))
                //聚合函数
                .innerJoin(DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION).on(DERIVE.ID.eq(DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION.DERIVE_ID))
                .innerJoin(AGGREGATE_FUNCTION_PARAM).on(AGGREGATE_FUNCTION_PARAM.ID.eq(DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION.AGGREGATE_FUNCTION_PARAM_ID))
                .innerJoin(AGGREGATE_FUNCTION).on(AGGREGATE_FUNCTION.ID.eq(AGGREGATE_FUNCTION_PARAM.AGGREGATE_FUNCTION_ID))
                //窗口参数
                .innerJoin(DERIVE_WINDOW_PARAM_RELATION).on(DERIVE.ID.eq(DERIVE_WINDOW_PARAM_RELATION.DERIVE_ID))
                .innerJoin(WINDOW_PARAM).on(DERIVE_WINDOW_PARAM_RELATION.WINDOW_PARAM_ID.eq(WINDOW_PARAM.ID))
                //过滤派生指标名
                .where(DERIVE.NAME.like("test_cov"))
                //过滤中文名
                .and(DERIVE.DISPLAY_NAME.like("测试协方差"))
                //过滤数据明细宽表名
                .and(MODEL.NAME.like("trade_detail2"))
                //过滤数据明细宽表中文名
                .and(MODEL.DISPLAY_NAME.like("交易流水表2"))
                //过滤聚合函数名字
                .and(AGGREGATE_FUNCTION.DISPLAY_NAME.like("协方差"))
                //过滤时间字段格式
                .and(MODEL_TIME_COLUMN.TIME_FORMAT.like("TIMESTAMP"))
                //过滤时间字段名
                .and(MODEL_COLUMN.NAME.as("time_column." + MODEL_COLUMN.NAME.getName()).like("trans_timestamp"))
                //过滤维度字段名
                //过滤维度名称
                //过滤窗口类型
                .and(WINDOW_PARAM.WINDOW_TYPE.eq(TUMBLING_TIME_WINDOW))
                .and(DERIVE.ID.in(List.of(32, 33)));
        List<Derive> derives = deriveMapper.selectListWithRelationsByQuery(queryWrapper);
        System.out.println(derives);
    }

}
