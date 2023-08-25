package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.Dimension;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.DimensionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@Tag(name = "维度管理")
@RequestMapping("/dimension")
public class DimensionController {

    @Autowired
    private DimensionService dimensionService;

    @PostMapping("/save")
    @Operation(summary = "新增维度")
    public Result<Void> save(@RequestBody Dimension dimension) {
        dimensionService.save(dimension);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除维度")
    public Result<Void> remove(@PathVariable Serializable id) {
        dimensionService.removeById(id);
        return Result.ok();
    }

    @PutMapping("/update")
    @Operation(summary = "修改维度")
    public Result<Void> update(@RequestBody Dimension dimension) {
        dimensionService.updateById(dimension);
        return Result.ok();
    }

    @GetMapping("/list")
    @Operation(summary = "维度列表")
    public Result<List<Dimension>> list() {
        return Result.ok(dimensionService.list());
    }

    @GetMapping("/getInfo/{id}")
    @Operation(summary = "维度详情")
    public Result<Dimension> getInfo(@PathVariable Integer id) {
        return Result.ok(dimensionService.getById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "维度分页")
    public Result<Page<Dimension>> page(Page<Dimension> page) {
        return Result.ok(dimensionService.page(page));
    }

}