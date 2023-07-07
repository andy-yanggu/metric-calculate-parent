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
import com.yanggu.metric_calculate.config.entity.AggregateFunctionParam;
import com.yanggu.metric_calculate.config.service.AggregateFunctionParamService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 *  控制层。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/aggregateFunctionParam")
public class AggregateFunctionParamController {

    @Autowired
    private AggregateFunctionParamService aggregateFunctionParamService;

    /**
     * 添加。
     *
     * @param aggregateFunctionParam 
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AggregateFunctionParam aggregateFunctionParam) {
        return aggregateFunctionParamService.save(aggregateFunctionParam);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aggregateFunctionParamService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param aggregateFunctionParam 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AggregateFunctionParam aggregateFunctionParam) {
        return aggregateFunctionParamService.updateById(aggregateFunctionParam);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AggregateFunctionParam> list() {
        return aggregateFunctionParamService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public AggregateFunctionParam getInfo(@PathVariable Serializable id) {
        return aggregateFunctionParamService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AggregateFunctionParam> page(Page<AggregateFunctionParam> page) {
        return aggregateFunctionParamService.page(page);
    }

}