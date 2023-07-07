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
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 宽表字段 控制层。
 *
 * @author MondayLi
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/modelColumn")
public class ModelColumnController {

    @Autowired
    private ModelColumnService modelColumnService;

    /**
     * 添加宽表字段。
     *
     * @param modelColumn 宽表字段
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ModelColumn modelColumn) {
        return modelColumnService.save(modelColumn);
    }

    /**
     * 根据主键删除宽表字段。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return modelColumnService.removeById(id);
    }

    /**
     * 根据主键更新宽表字段。
     *
     * @param modelColumn 宽表字段
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ModelColumn modelColumn) {
        return modelColumnService.updateById(modelColumn);
    }

    /**
     * 查询所有宽表字段。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ModelColumn> list() {
        return modelColumnService.list();
    }

    /**
     * 根据宽表字段主键获取详细信息。
     *
     * @param id 宽表字段主键
     * @return 宽表字段详情
     */
    @GetMapping("getInfo/{id}")
    public ModelColumn getInfo(@PathVariable Serializable id) {
        return modelColumnService.getById(id);
    }

    /**
     * 分页查询宽表字段。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ModelColumn> page(Page<ModelColumn> page) {
        return modelColumnService.page(page);
    }

}