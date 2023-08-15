package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.Dimension;
import com.yanggu.metric_calculate.config.service.DimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 维度表 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/dimension")
public class DimensionController {

    @Autowired
    private DimensionService dimensionService;

    /**
     * 添加维度表。
     *
     * @param dimension 维度表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Dimension dimension) {
        return dimensionService.save(dimension);
    }

    /**
     * 根据主键删除维度表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return dimensionService.removeById(id);
    }

    /**
     * 根据主键更新维度表。
     *
     * @param dimension 维度表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Dimension dimension) {
        return dimensionService.updateById(dimension);
    }

    /**
     * 查询所有维度表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<Dimension> list() {
        return dimensionService.list();
    }

    /**
     * 根据维度表主键获取详细信息。
     *
     * @param id 维度表主键
     * @return 维度表详情
     */
    @GetMapping("getInfo/{id}")
    public Dimension getInfo(@PathVariable Serializable id) {
        return dimensionService.getById(id);
    }

    /**
     * 分页查询维度表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<Dimension> page(Page<Dimension> page) {
        return dimensionService.page(page);
    }

}