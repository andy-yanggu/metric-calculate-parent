package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionDto;
import com.yanggu.metric_calculate.config.pojo.req.DimensionQueryReq;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.DimensionService;
import com.yanggu.metric_calculate.config.util.excel.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "维度管理")
@RequestMapping("/dimension")
public class DimensionController {

    @Autowired
    private DimensionService dimensionService;

    @PostMapping("/saveData")
    @Operation(summary = "新增维度")
    public Result<Void> saveData(@RequestBody DimensionDto dimensionDto) {
        dimensionService.saveData(dimensionDto);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改维度")
    public Result<Void> updateData(@RequestBody DimensionDto dimensionDto) {
        dimensionService.updateData(dimensionDto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除维度")
    public Result<Void> remove(@PathVariable("id") Integer id) {
        dimensionService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "维度列表")
    public Result<List<DimensionDto>> listData(DimensionQueryReq req) {
        return Result.ok(dimensionService.listData(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "维度详情")
    public Result<DimensionDto> detail(@PathVariable("id") Integer id) {
        return Result.ok(dimensionService.queryById(id));
    }

    @GetMapping("/pageData")
    @Operation(summary = "维度分页")
    public Result<Page<DimensionDto>> pageData(Integer pageNumber, Integer pageSize, DimensionQueryReq req) {
        return Result.ok(dimensionService.pageData(pageNumber, pageSize, req));
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, DimensionQueryReq req) {
        List<DimensionDto> dimensionDtos = dimensionService.listData(req);
        ExcelUtil.exportFormList(response, dimensionDtos);
    }

}