package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.ModelTimeColumn;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.yanggu.metric_calculate.config.service.ModelTimeColumnService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 时间字段 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/modelTimeColumn")
public class ModelTimeColumnController {

    @Autowired
    private ModelTimeColumnService modelTimeColumnService;

    /**
     * 添加时间字段。
     *
     * @param modelTimeColumn 时间字段
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ModelTimeColumn modelTimeColumn) {
        return modelTimeColumnService.save(modelTimeColumn);
    }

    /**
     * 根据主键删除时间字段。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return modelTimeColumnService.removeById(id);
    }

    /**
     * 根据主键更新时间字段。
     *
     * @param modelTimeColumn 时间字段
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ModelTimeColumn modelTimeColumn) {
        return modelTimeColumnService.updateById(modelTimeColumn);
    }

    /**
     * 查询所有时间字段。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ModelTimeColumn> list() {
        return modelTimeColumnService.list();
    }

    /**
     * 根据时间字段主键获取详细信息。
     *
     * @param id 时间字段主键
     * @return 时间字段详情
     */
    @GetMapping("getInfo/{id}")
    public ModelTimeColumn getInfo(@PathVariable Serializable id) {
        return modelTimeColumnService.getById(id);
    }

    /**
     * 分页查询时间字段。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ModelTimeColumn> page(Page<ModelTimeColumn> page) {
        return modelTimeColumnService.page(page);
    }

}