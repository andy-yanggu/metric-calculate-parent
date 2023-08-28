package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.AggregateFunctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@RestController
@Tag(name = "聚合函数管理")
@RequestMapping("/aggregateFunction")
public class AggregateFunctionController {

    @Autowired
    private AggregateFunctionService aggregateFunctionService;

    @PostMapping("/save")
    @Operation(summary = "新增聚合函数")
    public Result<Void> save(@RequestBody AggregateFunctionDto aggregateFunction) throws Exception {
        aggregateFunctionService.saveData(aggregateFunction);
        return Result.ok();
    }

    @PostMapping("/jarSave")
    @Operation(summary = "通过jar文件保存")
    public Result<Void> jarSave(@RequestParam("file") MultipartFile file) throws Exception {
        aggregateFunctionService.jarSave(file);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除聚合函数")
    public Result<Void> remove(@PathVariable Serializable id) {
        aggregateFunctionService.removeById(id);
        return Result.ok();
    }

    @PutMapping("/update")
    @Operation(summary = "修改聚合函数")
    public Result<Void> update(@RequestBody AggregateFunction aggregateFunction) {
        aggregateFunctionService.updateById(aggregateFunction);
        return Result.ok();
    }

    @GetMapping("/list")
    @Operation(summary = "聚合函数列表")
    public Result<List<AggregateFunctionDto>> list() {
        return Result.ok(aggregateFunctionService.listData());
    }

    @GetMapping("/getInfo/{id}")
    @Operation(summary = "聚合函数详情")
    public Result<AggregateFunctionDto> getInfo(@PathVariable Integer id) {
        return Result.ok(aggregateFunctionService.queryById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "聚合函数分页")
    public Result<Page<AggregateFunction>> page(Page<AggregateFunction> page) {
        return Result.ok(aggregateFunctionService.page(page));
    }

}