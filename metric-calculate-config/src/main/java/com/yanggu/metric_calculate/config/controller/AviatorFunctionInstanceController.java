package com.yanggu.metric_calculate.config.controller;

import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;
import com.yanggu.metric_calculate.config.domain.dto.AviatorFunctionInstanceDTO;
import com.yanggu.metric_calculate.config.domain.query.AviatorFunctionInstanceQuery;
import com.yanggu.metric_calculate.config.domain.vo.AviatorFunctionInstanceVO;
import com.yanggu.metric_calculate.config.service.AviatorFunctionInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<AviatorFunctionInstanceVO> listData(AviatorFunctionInstanceQuery query) {
        return aviatorFunctionInstanceService.listData(query);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Aviator函数实例详情")
    public AviatorFunctionInstanceVO detail(@PathVariable("id") Integer id) {
        return aviatorFunctionInstanceService.queryById(id);
    }

    @GetMapping("/pageData")
    @Operation(summary = "Aviator函数实例分页")
    public PageVO<AviatorFunctionInstanceVO> pageData(AviatorFunctionInstanceQuery query) {
        return aviatorFunctionInstanceService.pageData(query);
    }

}
