package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDto;
import com.yanggu.metric_calculate.config.pojo.req.AviatorFunctionInstanceQueryReq;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.AviatorFunctionInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "Aviator函数实例管理")
@RequestMapping("/aviatorFunctionInstance")
public class AviatorFunctionInstanceController {

    @Autowired
    private AviatorFunctionInstanceService aviatorFunctionInstanceService;

    @PostMapping("/saveData")
    @Operation(summary = "新增Aviator函数实例")
    public Result<Void> saveData(@RequestBody AviatorFunctionInstanceDto aviatorFunctionInstanceDto) {
        aviatorFunctionInstanceService.saveData(aviatorFunctionInstanceDto);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改Aviator函数实例")
    public Result<Void> updateData(@RequestBody AviatorFunctionInstanceDto aviatorFunctionInstanceDto) {
        aviatorFunctionInstanceService.updateData(aviatorFunctionInstanceDto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Aviator函数实例")
    public Result<Void> deleteById(@PathVariable("id") Integer id) {
        aviatorFunctionInstanceService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "Aviator函数实例列表")
    public Result<List<AviatorFunctionInstanceDto>> listData(AviatorFunctionInstanceQueryReq req) {
        return Result.ok(aviatorFunctionInstanceService.listData(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Aviator函数实例详情")
    public Result<AviatorFunctionInstanceDto> detail(@PathVariable("id") Integer id) {
        return Result.ok(aviatorFunctionInstanceService.queryById(id));
    }

    @GetMapping("/pageData")
    @Operation(summary = "Aviator函数实例分页")
    public Result<Page<AviatorFunctionInstanceDto>> pageData(Integer pageNumber,
                                                             Integer pageSize,
                                                             AviatorFunctionInstanceQueryReq req) {
        return Result.ok(aviatorFunctionInstanceService.pageData(pageNumber, pageSize, req));
    }

}