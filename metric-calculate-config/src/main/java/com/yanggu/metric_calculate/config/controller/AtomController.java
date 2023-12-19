package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.AtomDto;
import com.yanggu.metric_calculate.config.pojo.req.AtomQueryReq;
import com.yanggu.metric_calculate.config.pojo.req.DeriveQueryReq;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.config.service.AtomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "原子指标管理")
@RequestMapping("/atom")
public class AtomController {
    
    @Autowired
    private AtomService atomService;

    @PostMapping("/saveData")
    @Operation(summary = "新增原子指标")
    public Result<Void> saveData(@RequestBody AtomDto atomDto) throws Exception {
        atomService.saveData(atomDto);
        return Result.ok();
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改原子指标")
    public Result<Void> updateData(@RequestBody AtomDto atomDto) throws Exception {
        atomService.updateData(atomDto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除原子指标")
    public Result<Void> remove(@PathVariable("id") Integer id) {
        atomService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/listData")
    @Operation(summary = "原子指标列表")
    public Result<List<AtomDto>> listData(AtomQueryReq atomQueryReq) {
        return Result.ok(atomService.listData(atomQueryReq));
    }

    @GetMapping("/{id}")
    @Operation(summary = "原子指标详情")
    public Result<AtomDto> detail(@PathVariable("id") Integer id) {
        return Result.ok(atomService.queryById(id));
    }

    @GetMapping("/pageQuery")
    @Operation(summary = "原子指标分页")
    public Result<Page<AtomDto>> pageQuery(Integer pageNumber, Integer pageSize, AtomQueryReq atomQueryReq) {
        return Result.ok(atomService.pageQuery(pageNumber, pageSize, atomQueryReq));
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, DeriveQueryReq req) {
        //List<DeriveDto> list = atomService.listData(req);
        //ExcelUtil.exportFormList(response, list);
    }

}
