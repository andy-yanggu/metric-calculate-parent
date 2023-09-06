package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.tenant.TenantManager;
import com.yanggu.metric_calculate.config.mapstruct.ModelMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.Model;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "数据明细宽表")
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelMapstruct modelMapstruct;

    @PostMapping("/saveData")
    @Operation(summary = "新增数据明细宽表")
    public Result<Void> saveData(@RequestBody ModelDto modelDto) throws Exception {
        modelService.saveData(modelDto);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改数据明细宽表")
    public Result<Void> updateData(@RequestBody ModelDto modelDto) {
        modelService.updateData(modelDto);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除数据明细宽表")
    public Result<Void> remove(@PathVariable Integer id) {
        modelService.removeById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "数据明细宽表列表")
    public Result<List<Model>> listData() {
        return Result.ok(modelService.list());
    }

    @GetMapping("/getInfo/{id}")
    @Operation(summary = "数据明细宽表详情")
    public Result<ModelDto> getInfo(@PathVariable Integer id) {
        return Result.ok(modelService.queryById(id));
    }

    @GetMapping("/pageData")
    @Operation(summary = "数据明细宽表分页")
    public Result<Page<Model>> pageData(Page<Model> page) {
        return Result.ok(modelService.page(page));
    }

    @GetMapping("/toCoreModel/{modelId}")
    @Operation(summary = "转换成核心宽表", description = "转换成core包中的Model")
    public Result<com.yanggu.metric_calculate.core.pojo.data_detail_table.Model> getCoreModel(@PathVariable Integer modelId) {
        Model model = TenantManager.withoutTenantCondition(() -> modelService.getMapper().selectOneWithRelationsById(modelId));
        return Result.ok(modelMapstruct.toCoreModel(model));
    }

    @GetMapping("/allCoreModel")
    @Operation(summary = "转换所有核心宽表", description = "转换成core包中的Model")
    public Result<List<com.yanggu.metric_calculate.core.pojo.data_detail_table.Model>> getAllCoreModel() {
        List<Model> modelList = TenantManager.withoutTenantCondition(() -> modelService.getMapper().selectAllWithRelations());
        return Result.ok(modelMapstruct.toCoreModel(modelList));
    }

}