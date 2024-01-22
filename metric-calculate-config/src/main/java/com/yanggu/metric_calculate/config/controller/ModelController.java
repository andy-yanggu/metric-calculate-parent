package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDTO;
import com.yanggu.metric_calculate.config.pojo.query.ModelQuery;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.ModelService;
import com.yanggu.metric_calculate.config.util.excel.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "数据明细宽表")
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @PostMapping("/saveData")
    @Operation(summary = "新增数据明细宽表")
    public Result<Void> saveData(@RequestBody ModelDTO modelDto) throws Exception {
        modelService.saveData(modelDto);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改数据明细宽表", description = "修改中文名、描述信息等")
    public Result<Void> updateData(@RequestBody ModelDTO modelDto) {
        modelService.updateData(modelDto);
        return Result.ok();
    }

    @PutMapping("/updateOtherData")
    @Operation(summary = "修改其他数据", description = "修改宽表字段、维度字段、时间字段")
    public Result<Void> updateOtherData(@RequestBody ModelDTO modelDto) {
        modelService.updateOtherData(modelDto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据明细宽表")
    public Result<Void> remove(@PathVariable("id") Integer id) {
        modelService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "数据明细宽表列表")
    public Result<List<ModelDTO>> listData(ModelQuery req) {
        return Result.ok(modelService.listData(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "数据明细宽表详情")
    public Result<ModelDTO> detail(@PathVariable("id") Integer id) {
        return Result.ok(modelService.queryById(id));
    }

    @GetMapping("/pageData")
    @Operation(summary = "数据明细宽表分页")
    public Result<Page<ModelDTO>> pageData(Integer pageNumber, Integer pageSize, ModelQuery req) {
        return Result.ok(modelService.pageData(pageNumber, pageSize, req));
    }

    @GetMapping("/getCoreModel/{modelId}")
    @Operation(summary = "转换成核心宽表", description = "转换成core包中的Model")
    public Result<com.yanggu.metric_calculate.core.pojo.data_detail_table.Model> getCoreModel(@PathVariable Integer modelId) {
        return Result.ok(modelService.toCoreModel(modelId));
    }

    @GetMapping("/allCoreModel")
    @Operation(summary = "转换所有核心宽表", description = "转换成core包中的Model")
    public Result<List<com.yanggu.metric_calculate.core.pojo.data_detail_table.Model>> getAllCoreModel() {
        return Result.ok(modelService.getAllCoreModel());
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, ModelQuery req) {
        List<ModelDTO> list = modelService.listData(req);
        ExcelUtil.exportFormList(response, list);
    }

}