package com.yanggu.metric_calculate.config.controller;

import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;
import com.yanggu.metric_calculate.config.domain.dto.AviatorFunctionDTO;
import com.yanggu.metric_calculate.config.domain.query.AviatorFunctionQuery;
import com.yanggu.metric_calculate.config.domain.vo.AviatorFunctionVO;
import com.yanggu.metric_calculate.config.service.AviatorFunctionService;
import com.yanggu.metric_calculate.config.util.excel.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public List<AviatorFunctionVO> listData(AviatorFunctionQuery query) {
        return aviatorFunctionService.listData(query);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Aviator函数详情")
    public AviatorFunctionVO detail(@PathVariable("id") Integer id) {
        return aviatorFunctionService.queryById(id);
    }

    @GetMapping("/pageData")
    @Operation(summary = "Aviator函数分页")
    public PageVO<AviatorFunctionVO> pageData(AviatorFunctionQuery query) {
        return aviatorFunctionService.pageData(query);
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, AviatorFunctionQuery query) {
        List<AviatorFunctionVO> list = aviatorFunctionService.listData(query);
        ExcelUtil.exportFormList(response, list);
    }

}
