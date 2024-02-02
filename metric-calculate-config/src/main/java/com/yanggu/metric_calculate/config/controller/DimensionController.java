package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionDTO;
import com.yanggu.metric_calculate.config.pojo.query.DimensionQuery;
import com.yanggu.metric_calculate.config.pojo.vo.DimensionVO;
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
    public void saveData(@RequestBody DimensionDTO dimensionDto) {
        dimensionService.saveData(dimensionDto);
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改维度")
    public void updateData(@RequestBody DimensionDTO dimensionDto) {
        dimensionService.updateData(dimensionDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除维度")
    public void remove(@PathVariable("id") Integer id) {
        dimensionService.deleteById(id);
    }

    @GetMapping("/listData")
    @Operation(summary = "维度列表")
    public List<DimensionVO> listData(DimensionQuery query) {
        return dimensionService.listData(query);
    }

    @GetMapping("/{id}")
    @Operation(summary = "维度详情")
    public DimensionVO detail(@PathVariable("id") Integer id) {
        return dimensionService.queryById(id);
    }

    @GetMapping("/pageData")
    @Operation(summary = "维度分页")
    public PageVO<DimensionVO> pageData(DimensionQuery query) {
        return dimensionService.pageData(query);
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, DimensionQuery query) {
        List<DimensionVO> dimensionDtos = dimensionService.listData(query);
        ExcelUtil.exportFormList(response, dimensionDtos);
    }

}