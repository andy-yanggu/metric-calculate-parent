package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDto;
import com.yanggu.metric_calculate.config.pojo.req.AggregateFunctionQueryReq;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.AggregateFunctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "聚合函数管理")
@RequestMapping("/aggregateFunction")
public class AggregateFunctionController {

    @Autowired
    private AggregateFunctionService aggregateFunctionService;

    @PostMapping("/saveData")
    @Operation(summary = "新增聚合函数")
    public Result<Void> saveData(@RequestBody AggregateFunctionDto aggregateFunctionDto) throws Exception {
        aggregateFunctionService.saveData(aggregateFunctionDto);
        return Result.ok();
    }

    @PostMapping("/jarSave")
    @Operation(summary = "通过jar文件保存")
    public Result<Void> jarSave(@RequestParam("file") MultipartFile file) throws Exception {
        aggregateFunctionService.jarSave(file);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改聚合函数")
    public Result<Void> updateData(@RequestBody AggregateFunctionDto aggregateFunctionDto) {
        aggregateFunctionService.updateData(aggregateFunctionDto);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除聚合函数")
    public Result<Void> deleteById(@PathVariable Integer id) {
        aggregateFunctionService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "聚合函数列表")
    public Result<List<AggregateFunctionDto>> listData(AggregateFunctionQueryReq queryReq) {
        return Result.ok(aggregateFunctionService.listData(queryReq));
    }

    @GetMapping("/getInfo/{id}")
    @Operation(summary = "聚合函数详情")
    public Result<AggregateFunctionDto> getInfo(@PathVariable Integer id) {
        return Result.ok(aggregateFunctionService.queryById(id));
    }

    @GetMapping("/pageQuery")
    @Operation(summary = "聚合函数分页")
    public Result<Page<AggregateFunctionDto>> pageQuery(Integer pageNumber,
                                                        Integer pageSize,
                                                        AggregateFunctionQueryReq queryReq) {
        return Result.ok(aggregateFunctionService.pageQuery(pageNumber, pageSize, queryReq));
    }

}