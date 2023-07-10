package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumnItem;
import com.yanggu.metric_calculate.config.service.DimensionColumnItemService;
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
    private DimensionColumnItemService dimensionColumnItemService;

    /**
     * 添加维度字段选项。
     *
     * @param dimensionColumnItem 维度字段选项
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody DimensionColumnItem dimensionColumnItem) {
        return dimensionColumnItemService.save(dimensionColumnItem);
    }

    /**
     * 根据主键删除维度字段选项。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return dimensionColumnItemService.removeById(id);
    }

    /**
     * 根据主键更新维度字段选项。
     *
     * @param dimensionColumnItem 维度字段选项
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody DimensionColumnItem dimensionColumnItem) {
        return dimensionColumnItemService.updateById(dimensionColumnItem);
    }

    /**
     * 查询所有维度字段选项。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<DimensionColumnItem> list() {
        return dimensionColumnItemService.list();
    }

    /**
     * 根据维度字段选项主键获取详细信息。
     *
     * @param id 维度字段选项主键
     * @return 维度字段选项详情
     */
    @GetMapping("getInfo/{id}")
    public DimensionColumnItem getInfo(@PathVariable Serializable id) {
        return dimensionColumnItemService.getById(id);
    }

    /**
     * 分页查询维度字段选项。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<DimensionColumnItem> page(Page<DimensionColumnItem> page) {
        return dimensionColumnItemService.page(page);
    }

}