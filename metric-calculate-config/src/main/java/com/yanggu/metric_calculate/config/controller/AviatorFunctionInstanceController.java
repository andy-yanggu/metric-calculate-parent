package com.yanggu.metric_calculate.config.controller;

import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDTO;
import com.yanggu.metric_calculate.config.pojo.query.AviatorFunctionInstanceQuery;
import com.yanggu.metric_calculate.config.pojo.vo.AviatorFunctionInstanceVO;
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
    public void saveData(@RequestBody AviatorFunctionInstanceDTO aviatorFunctionInstanceDto) {
        aviatorFunctionInstanceService.saveData(aviatorFunctionInstanceDto);
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改Aviator函数实例")
    public void updateData(@RequestBody AviatorFunctionInstanceDTO aviatorFunctionInstanceDto) {
        aviatorFunctionInstanceService.updateData(aviatorFunctionInstanceDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Aviator函数实例")
    public void deleteById(@PathVariable("id") Integer id) {
        aviatorFunctionInstanceService.deleteById(id);
    }

    @GetMapping("/listData")
    @Operation(summary = "Aviator函数实例列表")
    public List<AviatorFunctionInstanceVO> listData(AviatorFunctionInstanceQuery req) {
        return aviatorFunctionInstanceService.listData(req);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Aviator函数实例详情")
    public AviatorFunctionInstanceVO detail(@PathVariable("id") Integer id) {
        return aviatorFunctionInstanceService.queryById(id);
    }

    @GetMapping("/pageData")
    @Operation(summary = "Aviator函数实例分页")
    public PageVO<AviatorFunctionInstanceVO> pageData(AviatorFunctionInstanceQuery req) {
        return aviatorFunctionInstanceService.pageData(req);
    }

}