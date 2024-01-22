package com.yanggu.metric_calculate.config.controller;

import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDTO;
import com.yanggu.metric_calculate.config.pojo.query.AggregateFunctionQuery;
import com.yanggu.metric_calculate.config.pojo.vo.AggregateFunctionVO;
import com.yanggu.metric_calculate.config.service.AggregateFunctionService;
import com.yanggu.metric_calculate.config.util.excel.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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
    public void saveData(@RequestBody AggregateFunctionDTO aggregateFunctionDto) throws Exception {
        aggregateFunctionService.saveData(aggregateFunctionDto);
    }

    @PostMapping("/jarSave")
    @Operation(summary = "通过jar文件保存")
    public void jarSave(@RequestParam("file") MultipartFile file) throws Exception {
        aggregateFunctionService.jarSave(file);
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改聚合函数")
    public void updateData(@RequestBody AggregateFunctionDTO aggregateFunctionDto) {
        aggregateFunctionService.updateData(aggregateFunctionDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除聚合函数")
    public void deleteById(@PathVariable("id") Integer id) {
        aggregateFunctionService.deleteById(id);
    }

    @GetMapping("/listData")
    @Operation(summary = "聚合函数列表")
    public List<AggregateFunctionVO> listData(AggregateFunctionQuery queryReq) {
        return aggregateFunctionService.listData(queryReq);
    }

    @GetMapping("/{id}")
    @Operation(summary = "聚合函数详情")
    public AggregateFunctionVO detail(@PathVariable("id") Integer id) {
        return aggregateFunctionService.queryById(id);
    }

    @GetMapping("/pageQuery")
    @Operation(summary = "聚合函数分页")
    public PageVO<AggregateFunctionVO> pageQuery(AggregateFunctionQuery queryReq) {
        return aggregateFunctionService.pageQuery(queryReq);
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, AggregateFunctionQuery req) {
        List<AggregateFunctionVO> list = aggregateFunctionService.listData(req);
        ExcelUtil.exportFormList(response, list);
    }

}