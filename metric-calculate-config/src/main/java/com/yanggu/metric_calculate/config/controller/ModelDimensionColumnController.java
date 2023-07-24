package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.ModelDimensionColumn;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.yanggu.metric_calculate.config.service.ModelDimensionColumnService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 维度字段 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/modelDimensionColumn")
public class ModelDimensionColumnController {

    @Autowired
    private ModelDimensionColumnService dimensionColumnService;

    /**
     * 添加维度字段。
     *
     * @param modelDimensionColumn 维度字段
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ModelDimensionColumn modelDimensionColumn) {
        return dimensionColumnService.save(modelDimensionColumn);
    }

    /**
     * 根据主键删除维度字段。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return dimensionColumnService.removeById(id);
    }

    /**
     * 根据主键更新维度字段。
     *
     * @param modelDimensionColumn 维度字段
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ModelDimensionColumn modelDimensionColumn) {
        return dimensionColumnService.updateById(modelDimensionColumn);
    }

    /**
     * 查询所有维度字段。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ModelDimensionColumn> list() {
        return dimensionColumnService.list();
    }

    /**
     * 根据维度字段主键获取详细信息。
     *
     * @param id 维度字段主键
     * @return 维度字段详情
     */
    @GetMapping("getInfo/{id}")
    public ModelDimensionColumn getInfo(@PathVariable Serializable id) {
        return dimensionColumnService.getById(id);
    }

    /**
     * 分页查询维度字段。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ModelDimensionColumn> page(Page<ModelDimensionColumn> page) {
        return dimensionColumnService.page(page);
    }

}