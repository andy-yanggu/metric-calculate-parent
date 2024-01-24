package com.yanggu.metric_calculate.config.controller;

import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionDTO;
import com.yanggu.metric_calculate.config.pojo.query.AviatorFunctionQuery;
import com.yanggu.metric_calculate.config.pojo.vo.AviatorFunctionVO;
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
    public void saveData(@RequestBody AviatorFunctionDTO aviatorFunctionDto) throws Exception {
        aviatorFunctionService.saveData(aviatorFunctionDto);
    }

    @PostMapping("/jarSave")
    @Operation(summary = "通过jar文件保存")
    public void jarSave(@RequestParam("file") MultipartFile file) throws Exception {
        aviatorFunctionService.jarSave(file);
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改Aviator函数")
    public void updateData(@RequestBody AviatorFunctionDTO aviatorFunctionDto) {
        aviatorFunctionService.updateData(aviatorFunctionDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Aviator函数")
    public void deleteById(@PathVariable("id") Integer id) {
        aviatorFunctionService.deleteById(id);
    }

    @GetMapping("/listData")
    @Operation(summary = "Aviator函数函数列表")
    public List<AviatorFunctionVO> listData(AviatorFunctionQuery req) {
        return aviatorFunctionService.listData(req);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Aviator函数详情")
    public AviatorFunctionVO detail(@PathVariable("id") Integer id) {
        return aviatorFunctionService.queryById(id);
    }

    @GetMapping("/pageData")
    @Operation(summary = "Aviator函数分页")
    public PageVO<AviatorFunctionVO> pageData(AviatorFunctionQuery req) {
        return aviatorFunctionService.pageData(req);
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, AviatorFunctionQuery req) {
        List<AviatorFunctionVO> list = aviatorFunctionService.listData(req);
        ExcelUtil.exportFormList(response, list);
    }

}