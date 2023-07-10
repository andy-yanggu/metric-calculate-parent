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
import com.yanggu.metric_calculate.config.pojo.entity.Model;
import com.yanggu.metric_calculate.config.service.ModelService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 数据明细宽表 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ModelService modelService;

    /**
     * 添加数据明细宽表。
     *
     * @param model 数据明细宽表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Model model) {
        return modelService.save(model);
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
    public boolean update(@RequestBody Model model) {
        return modelService.updateById(model);
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
    @GetMapping("getInfo/{id}")
    public Model getInfo(@PathVariable Serializable id) {
        return modelService.getById(id);
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