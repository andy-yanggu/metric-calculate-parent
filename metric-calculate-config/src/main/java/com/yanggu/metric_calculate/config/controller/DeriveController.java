package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import com.yanggu.metric_calculate.config.pojo.req.DeriveQueryReq;
import com.yanggu.metric_calculate.config.pojo.vo.DeriveMetricsConfigData;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.DeriveService;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "派生指标管理")
@RequestMapping("/derive")
public class DeriveController {

    @Autowired
    private DeriveService deriveService;

    @Autowired
    private DeriveMapstruct deriveMapstruct;

    @PostMapping("/saveData")
    @Operation(summary = "新增派生指标")
    public Result<Void> saveData(@RequestBody DeriveDto deriveDto) throws Exception {
        deriveService.saveData(deriveDto);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改派生指标")
    public Result<Void> updateData(@RequestBody DeriveDto deriveDto) throws Exception {
        deriveService.updateData(deriveDto);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除派生指标")
    public Result<Void> remove(@PathVariable Integer id) {
        deriveService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "派生指标列表")
    public Result<List<DeriveDto>> listData(DeriveQueryReq deriveQuery) {
        return Result.ok(deriveService.listData(deriveQuery));
    }

    @GetMapping("/getInfo/{id}")
    @Operation(summary = "派生指标详情")
    public Result<DeriveDto> getInfo(@PathVariable Integer id) {
        return Result.ok(deriveService.queryById(id));
    }

    @GetMapping("/pageQuery")
    @Operation(summary = "派生指标分页")
    public Result<Page<DeriveDto>> pageQuery(Integer pageNumber, Integer pageSize, DeriveQueryReq deriveQuery) {
        return Result.ok(deriveService.pageQuery(pageNumber, pageSize, deriveQuery));
    }

    @GetMapping("/toCoreDeriveMetrics/{deriveId}")
    @Operation(summary = "转换成核心派生指标")
    public Result<DeriveMetrics> getDeriveMetrics(@PathVariable Integer deriveId) {
        Derive derive = deriveService.getMapper().selectOneWithRelationsById(deriveId);
        DeriveMetrics deriveMetrics = deriveMapstruct.toDeriveMetrics(derive);
        return Result.ok(deriveMetrics);
    }

    @GetMapping("/allCoreDeriveMetrics")
    @Operation(summary = "转换成所有核心派生指标")
    public Result<List<DeriveMetricsConfigData>> getAllCoreDeriveMetrics() {
        List<DeriveMetricsConfigData> list = deriveService.getAllCoreDeriveMetrics();
        return Result.ok(list);
    }

}