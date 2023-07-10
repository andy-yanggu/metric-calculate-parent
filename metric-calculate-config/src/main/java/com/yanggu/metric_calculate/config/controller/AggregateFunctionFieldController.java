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
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionField;
import com.yanggu.metric_calculate.config.service.AggregateFunctionFieldService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合函数的字段 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/aggregateFunctionField")
public class AggregateFunctionFieldController {

    @Autowired
    private AggregateFunctionFieldService aggregateFunctionFieldService;

    /**
     * 添加聚合函数的字段。
     *
     * @param aggregateFunctionField 聚合函数的字段
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AggregateFunctionField aggregateFunctionField) {
        return aggregateFunctionFieldService.save(aggregateFunctionField);
    }

    /**
     * 根据主键删除聚合函数的字段。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aggregateFunctionFieldService.removeById(id);
    }

    /**
     * 根据主键更新聚合函数的字段。
     *
     * @param aggregateFunctionField 聚合函数的字段
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AggregateFunctionField aggregateFunctionField) {
        return aggregateFunctionFieldService.updateById(aggregateFunctionField);
    }

    /**
     * 查询所有聚合函数的字段。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AggregateFunctionField> list() {
        return aggregateFunctionFieldService.list();
    }

    /**
     * 根据聚合函数的字段主键获取详细信息。
     *
     * @param id 聚合函数的字段主键
     * @return 聚合函数的字段详情
     */
    @GetMapping("getInfo/{id}")
    public AggregateFunctionField getInfo(@PathVariable Serializable id) {
        return aggregateFunctionFieldService.getById(id);
    }

    /**
     * 分页查询聚合函数的字段。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AggregateFunctionField> page(Page<AggregateFunctionField> page) {
        return aggregateFunctionFieldService.page(page);
    }

}