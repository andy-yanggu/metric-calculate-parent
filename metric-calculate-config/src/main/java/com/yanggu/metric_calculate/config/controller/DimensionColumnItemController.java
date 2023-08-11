package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.DeriveModelDimensionColumnRelation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.yanggu.metric_calculate.config.service.DeriveModelDimensionColumnRelationService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 维度字段选项 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/dimensionColumnItem")
public class DimensionColumnItemController {

    @Autowired
    private DeriveModelDimensionColumnRelationService deriveModelDimensionColumnRelationService;

    /**
     * 添加维度字段选项。
     *
     * @param deriveModelDimensionColumnRelation 维度字段选项
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody DeriveModelDimensionColumnRelation deriveModelDimensionColumnRelation) {
        return deriveModelDimensionColumnRelationService.save(deriveModelDimensionColumnRelation);
    }

    /**
     * 根据主键删除维度字段选项。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return deriveModelDimensionColumnRelationService.removeById(id);
    }

    /**
     * 根据主键更新维度字段选项。
     *
     * @param deriveModelDimensionColumnRelation 维度字段选项
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody DeriveModelDimensionColumnRelation deriveModelDimensionColumnRelation) {
        return deriveModelDimensionColumnRelationService.updateById(deriveModelDimensionColumnRelation);
    }

    /**
     * 查询所有维度字段选项。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<DeriveModelDimensionColumnRelation> list() {
        return deriveModelDimensionColumnRelationService.list();
    }

    /**
     * 根据维度字段选项主键获取详细信息。
     *
     * @param id 维度字段选项主键
     * @return 维度字段选项详情
     */
    @GetMapping("getInfo/{id}")
    public DeriveModelDimensionColumnRelation getInfo(@PathVariable Serializable id) {
        return deriveModelDimensionColumnRelationService.getById(id);
    }

    /**
     * 分页查询维度字段选项。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<DeriveModelDimensionColumnRelation> page(Page<DeriveModelDimensionColumnRelation> page) {
        return deriveModelDimensionColumnRelationService.page(page);
    }

}