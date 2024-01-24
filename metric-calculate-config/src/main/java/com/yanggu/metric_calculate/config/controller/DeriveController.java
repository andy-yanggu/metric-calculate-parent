package com.yanggu.metric_calculate.config.controller;

import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDTO;
import com.yanggu.metric_calculate.config.pojo.query.DeriveQuery;
import com.yanggu.metric_calculate.config.pojo.vo.DeriveMetricsConfigData;
import com.yanggu.metric_calculate.config.pojo.vo.DeriveVO;
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
    public void saveData(@RequestBody DeriveDTO deriveDto) throws Exception {
        deriveService.saveData(deriveDto);
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改派生指标")
    public void updateData(@RequestBody DeriveDTO deriveDto) throws Exception {
        deriveService.updateData(deriveDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除派生指标")
    public void remove(@PathVariable("id") Integer id) {
        deriveService.deleteById(id);
    }

    @GetMapping("/listData")
    @Operation(summary = "派生指标列表")
    public List<DeriveVO> listData(DeriveQuery deriveQuery) {
        return deriveService.listData(deriveQuery);
    }

    @GetMapping("/{id}")
    @Operation(summary = "派生指标详情")
    public DeriveVO detail(@PathVariable("id") Integer id) {
        return deriveService.queryById(id);
    }

    @GetMapping("/pageQuery")
    @Operation(summary = "派生指标分页")
    public PageVO<DeriveVO> pageQuery(DeriveQuery deriveQuery) {
        return deriveService.pageQuery(deriveQuery);
    }

    @GetMapping("/toCoreDeriveMetrics/{deriveId}")
    @Operation(summary = "转换成核心派生指标")
    public DeriveMetrics toCoreDeriveMetrics(@PathVariable("deriveId") Integer deriveId) {
        return deriveService.toCoreDeriveMetrics(deriveId);
    }

    @GetMapping("/allCoreDeriveMetrics")
    @Operation(summary = "转换成所有核心派生指标")
    public List<DeriveMetricsConfigData> getAllCoreDeriveMetrics() {
        return deriveService.getAllCoreDeriveMetrics();
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, DeriveQuery req) {
        List<DeriveVO> list = deriveService.listData(req);
        ExcelUtil.exportFormList(response, list);
    }

}