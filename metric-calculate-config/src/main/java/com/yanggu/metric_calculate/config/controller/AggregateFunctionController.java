package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.service.AggregateFunctionService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合函数 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/aggregateFunction")
public class AggregateFunctionController {

    @Autowired
    private AggregateFunctionService aggregateFunctionService;

    /**
     * 添加聚合函数。
     *
     * @param aggregateFunction 聚合函数
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AggregateFunction aggregateFunction) {
        return aggregateFunctionService.save(aggregateFunction);
    }

    /**
     * 根据主键删除聚合函数。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aggregateFunctionService.removeById(id);
    }

    /**
     * 根据主键更新聚合函数。
     *
     * @param aggregateFunction 聚合函数
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AggregateFunction aggregateFunction) {
        return aggregateFunctionService.updateById(aggregateFunction);
    }

    /**
     * 查询所有聚合函数。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AggregateFunction> list() {
        return aggregateFunctionService.list();
    }

    /**
     * 根据聚合函数主键获取详细信息。
     *
     * @param id 聚合函数主键
     * @return 聚合函数详情
     */
    @GetMapping("getInfo/{id}")
    public AggregateFunction getInfo(@PathVariable Serializable id) {
        return aggregateFunctionService.getById(id);
    }

    /**
     * 分页查询聚合函数。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AggregateFunction> page(Page<AggregateFunction> page) {
        return aggregateFunctionService.page(page);
    }

}