package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.DeriveService;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@Tag(name = "派生指标管理")
@RequestMapping("/derive")
public class DeriveController {

    @Autowired
    private DeriveService deriveService;

    @Autowired
    private DeriveMapstruct deriveMapstruct;

    @PostMapping("/save")
    @Operation(summary = "新增派生指标")
    public Result<Void> save(@RequestBody DeriveDto derive) throws Exception {
        deriveService.create(derive);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除派生指标")
    public Result<Void> remove(@PathVariable Serializable id) {
        deriveService.removeById(id);
        return Result.ok();
    }

    @PutMapping("/update")
    @Operation(summary = "修改派生指标")
    public Result<Void> update(@RequestBody Derive derive) {
        deriveService.updateById(derive);
        return Result.ok();
    }

    @GetMapping("/list")
    @Operation(summary = "派生指标列表")
    public Result<List<Derive>> list() {
        return Result.ok(deriveService.list());
    }

    @GetMapping("/getInfo/{id}")
    @Operation(summary = "派生指标详情")
    public Result<DeriveDto> getInfo(@PathVariable Integer id) {
        return Result.ok(deriveService.queryById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "派生指标分页")
    public Result<Page<Derive>> page(Page<Derive> page) {
        return Result.ok(deriveService.page(page));
    }

    @GetMapping("/test/{deriveId}")
    public Result<DeriveMetrics> getDeriveMetrics(@PathVariable Integer deriveId) {
        Derive derive = deriveService.getMapper().selectOneWithRelationsById(deriveId);
        DeriveMetrics deriveMetrics = deriveMapstruct.toDeriveMetrics(derive);
        return Result.ok(deriveMetrics);
    }

}