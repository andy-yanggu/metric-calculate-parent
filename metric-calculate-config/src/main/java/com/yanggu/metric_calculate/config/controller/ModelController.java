package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.yanggu.metric_calculate.config.pojo.entity.Model;
import com.yanggu.metric_calculate.config.service.ModelService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 数据明细宽表
 */
@RestController
@Tag(name = "数据明细宽表")
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ModelService modelService;

    /**
     * 添加数据明细宽表
     */
    @PostMapping("save")
    @Operation(summary = "新增数据明细宽表")
    public void save(@RequestBody ModelDto model) {
        modelService.create(model);
    }

    /**
     * 根据主键删除数据明细宽表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return modelService.removeById(id);
    }

    /**
     * 根据主键更新数据明细宽表。
     *
     * @param model 数据明细宽表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public void update(@RequestBody ModelDto model) {
        modelService.updateById(model);
    }

    /**
     * 查询所有数据明细宽表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<Model> list() {
        return modelService.list();
    }

    /**
     * 根据数据明细宽表主键获取详细信息。
     *
     * @param id 数据明细宽表主键
     * @return 数据明细宽表详情
     */
    @Operation(summary = "根据主键查询数据")
    @GetMapping("getInfo/{id}")
    public ModelDto getInfo(@PathVariable Integer id) {
        return modelService.queryById(id);
    }

    /**
     * 分页查询数据明细宽表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<Model> page(Page<Model> page) {
        return modelService.page(page);
    }

}