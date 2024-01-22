package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDTO;
import com.yanggu.metric_calculate.config.pojo.query.DeriveQuery;
import com.yanggu.metric_calculate.config.pojo.vo.DeriveMetricsConfigData;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.DeriveService;
import com.yanggu.metric_calculate.config.util.excel.ExcelUtil;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "派生指标管理")
@RequestMapping("/derive")
public class DeriveController {

    @Autowired
    private DeriveService deriveService;

    @PostMapping("/saveData")
    @Operation(summary = "新增派生指标")
    public Result<Void> saveData(@RequestBody DeriveDTO deriveDto) throws Exception {
        deriveService.saveData(deriveDto);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改派生指标")
    public Result<Void> updateData(@RequestBody DeriveDTO deriveDto) throws Exception {
        deriveService.updateData(deriveDto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除派生指标")
    public Result<Void> remove(@PathVariable("id") Integer id) {
        deriveService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "派生指标列表")
    public Result<List<DeriveDTO>> listData(DeriveQuery deriveQuery) {
        return Result.ok(deriveService.listData(deriveQuery));
    }

    @GetMapping("/{id}")
    @Operation(summary = "派生指标详情")
    public Result<DeriveDTO> detail(@PathVariable("id") Integer id) {
        return Result.ok(deriveService.queryById(id));
    }

    @GetMapping("/pageQuery")
    @Operation(summary = "派生指标分页")
    public Result<Page<DeriveDTO>> pageQuery(Integer pageNumber, Integer pageSize, DeriveQuery deriveQuery) {
        return Result.ok(deriveService.pageQuery(pageNumber, pageSize, deriveQuery));
    }

    @GetMapping("/toCoreDeriveMetrics/{deriveId}")
    @Operation(summary = "转换成核心派生指标")
    public Result<DeriveMetrics> toCoreDeriveMetrics(@PathVariable("deriveId") Integer deriveId) {
        DeriveMetrics deriveMetrics = deriveService.toCoreDeriveMetrics(deriveId);
        return Result.ok(deriveMetrics);
    }

    @GetMapping("/allCoreDeriveMetrics")
    @Operation(summary = "转换成所有核心派生指标")
    public Result<List<DeriveMetricsConfigData>> getAllCoreDeriveMetrics() {
        List<DeriveMetricsConfigData> list = deriveService.getAllCoreDeriveMetrics();
        return Result.ok(list);
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, DeriveQuery req) {
        List<DeriveDTO> list = deriveService.listData(req);
        ExcelUtil.exportFormList(response, list);
    }

}