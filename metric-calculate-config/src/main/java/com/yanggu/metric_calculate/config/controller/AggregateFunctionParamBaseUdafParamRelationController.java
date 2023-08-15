package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionParamBaseUdafParamRelation;
import com.yanggu.metric_calculate.config.service.AggregateFunctionParamBaseUdafParamRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合函数参数-基本聚合参数中间表 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/aggregateFunctionParamBaseUdafParamRelation")
public class AggregateFunctionParamBaseUdafParamRelationController {

    @Autowired
    private AggregateFunctionParamBaseUdafParamRelationService aggregateFunctionParamBaseUdafParamRelationService;

    /**
     * 添加聚合函数参数-基本聚合参数中间表。
     *
     * @param aggregateFunctionParamBaseUdafParamRelation 聚合函数参数-基本聚合参数中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AggregateFunctionParamBaseUdafParamRelation aggregateFunctionParamBaseUdafParamRelation) {
        return aggregateFunctionParamBaseUdafParamRelationService.save(aggregateFunctionParamBaseUdafParamRelation);
    }

    /**
     * 根据主键删除聚合函数参数-基本聚合参数中间表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aggregateFunctionParamBaseUdafParamRelationService.removeById(id);
    }

    /**
     * 根据主键更新聚合函数参数-基本聚合参数中间表。
     *
     * @param aggregateFunctionParamBaseUdafParamRelation 聚合函数参数-基本聚合参数中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AggregateFunctionParamBaseUdafParamRelation aggregateFunctionParamBaseUdafParamRelation) {
        return aggregateFunctionParamBaseUdafParamRelationService.updateById(aggregateFunctionParamBaseUdafParamRelation);
    }

    /**
     * 查询所有聚合函数参数-基本聚合参数中间表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AggregateFunctionParamBaseUdafParamRelation> list() {
        return aggregateFunctionParamBaseUdafParamRelationService.list();
    }

    /**
     * 根据聚合函数参数-基本聚合参数中间表主键获取详细信息。
     *
     * @param id 聚合函数参数-基本聚合参数中间表主键
     * @return 聚合函数参数-基本聚合参数中间表详情
     */
    @GetMapping("getInfo/{id}")
    public AggregateFunctionParamBaseUdafParamRelation getInfo(@PathVariable Serializable id) {
        return aggregateFunctionParamBaseUdafParamRelationService.getById(id);
    }

    /**
     * 分页查询聚合函数参数-基本聚合参数中间表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AggregateFunctionParamBaseUdafParamRelation> page(Page<AggregateFunctionParamBaseUdafParamRelation> page) {
        return aggregateFunctionParamBaseUdafParamRelationService.page(page);
    }

}