package com.yanggu.metric_calculate.config.controller;

import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.pojo.dto.AtomDTO;
import com.yanggu.metric_calculate.config.pojo.query.AtomQuery;
import com.yanggu.metric_calculate.config.pojo.query.DeriveQuery;
import com.yanggu.metric_calculate.config.pojo.vo.AtomVO;
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
    public void saveData(@RequestBody AtomDTO atomDto) throws Exception {
        atomService.saveData(atomDto);
    }

    @PutMapping("/updateData")
    @Operation(summary = "修改原子指标")
    public void updateData(@RequestBody AtomDTO atomDto) throws Exception {
        atomService.updateData(atomDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除原子指标")
    public void remove(@PathVariable("id") Integer id) {
        atomService.deleteById(id);
    }

    @GetMapping("/listData")
    @Operation(summary = "原子指标列表")
    public List<AtomVO> listData(AtomQuery atomQuery) {
        return atomService.listData(atomQuery);
    }

    @GetMapping("/{id}")
    @Operation(summary = "原子指标详情")
    public AtomVO detail(@PathVariable("id") Integer id) {
        return atomService.queryById(id);
    }

    @GetMapping("/pageQuery")
    @Operation(summary = "原子指标分页")
    public PageVO<AtomVO> pageQuery(AtomQuery atomQuery) {
        return atomService.pageQuery(atomQuery);
    }

    @GetMapping("/excelExport")
    @Operation(summary = "excel导出")
    public void excelExport(HttpServletResponse response, DeriveQuery req) {
        //List<DeriveDto> list = atomService.listData(req);
        //ExcelUtil.exportFormList(response, list);
    }

}
