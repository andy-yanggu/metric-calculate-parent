package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionDTO;
import com.yanggu.metric_calculate.config.pojo.query.DimensionQuery;
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
    public Result<Void> saveData(@RequestBody DimensionDTO dimensionDto) {
        dimensionService.saveData(dimensionDto);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改维度")
    public Result<Void> updateData(@RequestBody DimensionDTO dimensionDto) {
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
    public Result<List<DimensionDTO>> listData(DimensionQuery req) {
        return Result.ok(dimensionService.listData(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "维度详情")
    public Result<DimensionDTO> detail(@PathVariable("id") Integer id) {
        return Result.ok(dimensionService.queryById(id));
    }

    @GetMapping("/pageData")
    @Operation(summary = "维度分页")
    public Result<Page<DimensionDTO>> pageData(Integer pageNumber, Integer pageSize, DimensionQuery req) {
        return Result.ok(dimensionService.pageData(pageNumber, pageSize, req));
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, DimensionQuery req) {
        List<DimensionDTO> dimensionDtos = dimensionService.listData(req);
        ExcelUtil.exportFormList(response, dimensionDtos);
    }

}