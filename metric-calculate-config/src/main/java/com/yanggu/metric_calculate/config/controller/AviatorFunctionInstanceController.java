package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstance;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.AviatorFunctionInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


@RestController
@Tag(name = "Aviator函数实例管理")
@RequestMapping("/aviatorFunctionInstance")
public class AviatorFunctionInstanceController {

    @Autowired
    private AviatorFunctionInstanceService aviatorFunctionInstanceService;

    @PostMapping("/saveData")
    @Operation(summary = "新增Aviator函数实例")
    public Result<Void> saveData(@RequestBody AviatorFunctionInstanceDto aviatorFunctionInstance) {
        aviatorFunctionInstanceService.saveData(aviatorFunctionInstance);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改Aviator函数实例")
    public Result<Void> updateData(@RequestBody AviatorFunctionInstance aviatorFunctionInstance) {
        aviatorFunctionInstanceService.updateById(aviatorFunctionInstance);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除Aviator函数实例")
    public Result<Void> remove(@PathVariable Integer id) {
        aviatorFunctionInstanceService.removeById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "Aviator函数实例列表")
    public Result<List<AviatorFunctionInstance>> listData() {
        return Result.ok(aviatorFunctionInstanceService.list());
    }

    @GetMapping("/getInfo/{id}")
    @Operation(summary = "Aviator函数实例详情")
    public Result<AviatorFunctionInstance> getInfo(@PathVariable Serializable id) {
        return Result.ok(aviatorFunctionInstanceService.getById(id));
    }

    @GetMapping("/pageData")
    @Operation(summary = "Aviator函数实例分页")
    public Result<Page<AviatorFunctionInstance>> pageData(Page<AviatorFunctionInstance> page) {
        return Result.ok(aviatorFunctionInstanceService.page(page));
    }

}