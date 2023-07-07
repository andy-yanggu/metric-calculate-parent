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
import com.yanggu.metric_calculate.config.entity.AviatorFunctionInstance;
import com.yanggu.metric_calculate.config.service.AviatorFunctionInstanceService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * Aviator函数实例 控制层。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/aviatorFunctionInstance")
public class AviatorFunctionInstanceController {

    @Autowired
    private AviatorFunctionInstanceService aviatorFunctionInstanceService;

    /**
     * 添加Aviator函数实例。
     *
     * @param aviatorFunctionInstance Aviator函数实例
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AviatorFunctionInstance aviatorFunctionInstance) {
        return aviatorFunctionInstanceService.save(aviatorFunctionInstance);
    }

    /**
     * 根据主键删除Aviator函数实例。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aviatorFunctionInstanceService.removeById(id);
    }

    /**
     * 根据主键更新Aviator函数实例。
     *
     * @param aviatorFunctionInstance Aviator函数实例
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AviatorFunctionInstance aviatorFunctionInstance) {
        return aviatorFunctionInstanceService.updateById(aviatorFunctionInstance);
    }

    /**
     * 查询所有Aviator函数实例。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AviatorFunctionInstance> list() {
        return aviatorFunctionInstanceService.list();
    }

    /**
     * 根据Aviator函数实例主键获取详细信息。
     *
     * @param id Aviator函数实例主键
     * @return Aviator函数实例详情
     */
    @GetMapping("getInfo/{id}")
    public AviatorFunctionInstance getInfo(@PathVariable Serializable id) {
        return aviatorFunctionInstanceService.getById(id);
    }

    /**
     * 分页查询Aviator函数实例。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AviatorFunctionInstance> page(Page<AviatorFunctionInstance> page) {
        return aviatorFunctionInstanceService.page(page);
    }

}