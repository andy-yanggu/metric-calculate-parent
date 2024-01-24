package com.yanggu.metric_calculate.config.controller;

import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDTO;
import com.yanggu.metric_calculate.config.pojo.query.ModelQuery;
import com.yanggu.metric_calculate.config.pojo.vo.ModelVO;
import com.yanggu.metric_calculate.config.service.ModelService;
import com.yanggu.metric_calculate.config.util.excel.ExcelUtil;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
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
    public void saveData(@RequestBody ModelDTO modelDto) throws Exception {
        modelService.saveData(modelDto);
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改数据明细宽表", description = "修改中文名、描述信息等")
    public void updateData(@RequestBody ModelDTO modelDto) {
        modelService.updateData(modelDto);
    }

    @PutMapping("/updateOtherData")
    @Operation(summary = "修改其他数据", description = "修改宽表字段、维度字段、时间字段")
    public void updateOtherData(@RequestBody ModelDTO modelDto) {
        modelService.updateOtherData(modelDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据明细宽表")
    public void remove(@PathVariable("id") Integer id) {
        modelService.deleteById(id);
    }

    @GetMapping("/listData")
    @Operation(summary = "数据明细宽表列表")
    public List<ModelVO> listData(ModelQuery req) {
        return modelService.listData(req);
    }

    @GetMapping("/{id}")
    @Operation(summary = "数据明细宽表详情")
    public ModelVO detail(@PathVariable("id") Integer id) {
        return modelService.queryById(id);
    }

    @GetMapping("/pageData")
    @Operation(summary = "数据明细宽表分页")
    public PageVO<ModelVO> pageData(ModelQuery req) {
        return modelService.pageData(req);
    }

    @GetMapping("/getCoreModel/{modelId}")
    @Operation(summary = "转换成核心宽表", description = "转换成core包中的Model")
    public Model getCoreModel(@PathVariable Integer modelId) {
        return modelService.toCoreModel(modelId);
    }

    @GetMapping("/allCoreModel")
    @Operation(summary = "转换所有核心宽表", description = "转换成core包中的Model")
    public List<Model> getAllCoreModel() {
        return modelService.getAllCoreModel();
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, ModelQuery req) {
        List<ModelVO> list = modelService.listData(req);
        ExcelUtil.exportFormList(response, list);
    }

}