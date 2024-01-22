package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionDTO;
import com.yanggu.metric_calculate.config.pojo.query.AviatorFunctionQuery;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.AviatorFunctionService;
import com.yanggu.metric_calculate.config.util.excel.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "Aviator函数管理")
@RequestMapping("/aviatorFunction")
public class AviatorFunctionController {

    @Autowired
    private AviatorFunctionService aviatorFunctionService;

    @PostMapping("/saveData")
    @Operation(summary = "新增Aviator函数")
    public Result<Void> saveData(@RequestBody AviatorFunctionDTO aviatorFunctionDto) throws Exception {
        aviatorFunctionService.saveData(aviatorFunctionDto);
        return Result.ok();
    }

    @PostMapping("/jarSave")
    @Operation(summary = "通过jar文件保存")
    public Result<Void> jarSave(@RequestParam("file") MultipartFile file) throws Exception {
        aviatorFunctionService.jarSave(file);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改Aviator函数")
    public Result<Void> updateData(@RequestBody AviatorFunctionDTO aviatorFunctionDto) {
        aviatorFunctionService.updateData(aviatorFunctionDto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Aviator函数")
    public Result<Void> deleteById(@PathVariable("id") Integer id) {
        aviatorFunctionService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "Aviator函数函数列表")
    public Result<List<AviatorFunctionDTO>> listData(AviatorFunctionQuery req) {
        return Result.ok(aviatorFunctionService.listData(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Aviator函数详情")
    public Result<AviatorFunctionDTO> detail(@PathVariable("id") Integer id) {
        return Result.ok(aviatorFunctionService.queryById(id));
    }

    @GetMapping("/pageData")
    @Operation(summary = "Aviator函数分页")
    public Result<Page<AviatorFunctionDTO>> pageData(Integer pageNumber,
                                                     Integer pageSize,
                                                     AviatorFunctionQuery req) {
        return Result.ok(aviatorFunctionService.pageData(pageNumber, pageSize, req));
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, AviatorFunctionQuery req) {
        List<AviatorFunctionDTO> list = aviatorFunctionService.listData(req);
        ExcelUtil.exportFormList(response, list);
    }

}