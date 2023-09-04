package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunction;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.AviatorFunctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@RestController
@Tag(name = "Aviator函数管理")
@RequestMapping("/aviatorFunction")
public class AviatorFunctionController {

    @Autowired
    private AviatorFunctionService aviatorFunctionService;

    @PostMapping("/save")
    @Operation(summary = "新增Aviator函数")
    public Result<Void> save(@RequestBody AviatorFunctionDto aviatorFunctionDto) {
        aviatorFunctionService.saveData(aviatorFunctionDto);
        return Result.ok();
    }

    @PostMapping("/jarSave")
    @Operation(summary = "通过jar文件保存")
    public Result<Void> jarSave(@RequestParam("file") MultipartFile file) throws Exception {
        aviatorFunctionService.jarSave(file);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除Aviator函数")
    public Result<Void> remove(@PathVariable Integer id) {
        aviatorFunctionService.deleteById(id);
        return Result.ok();
    }

    @PutMapping("/update")
    @Operation(summary = "修改Aviator函数")
    public Result<Void> update(@RequestBody AviatorFunction aviatorFunction) {
        aviatorFunctionService.updateById(aviatorFunction);
        return Result.ok();
    }

    @GetMapping("/list")
    @Operation(summary = "Aviator函数函数列表")
    public Result<List<AviatorFunction>> list() {
        return Result.ok(aviatorFunctionService.list());
    }

    @GetMapping("/getInfo/{id}")
    @Operation(summary = "Aviator函数详情")
    public Result<AviatorFunction> getInfo(@PathVariable Serializable id) {
        return Result.ok(aviatorFunctionService.getById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "Aviator函数分页")
    public Result<Page<AviatorFunction>> page(Page<AviatorFunction> page) {
        return Result.ok(aviatorFunctionService.page(page));
    }

}