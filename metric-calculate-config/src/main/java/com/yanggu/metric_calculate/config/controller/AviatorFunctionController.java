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
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunction;
import com.yanggu.metric_calculate.config.service.AviatorFunctionService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * Aviator函数 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/aviatorFunction")
public class AviatorFunctionController {

    @Autowired
    private AviatorFunctionService aviatorFunctionService;

    /**
     * 添加Aviator函数。
     *
     * @param aviatorFunction Aviator函数
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AviatorFunction aviatorFunction) {
        return aviatorFunctionService.save(aviatorFunction);
    }

    /**
     * 根据主键删除Aviator函数。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aviatorFunctionService.removeById(id);
    }

    /**
     * 根据主键更新Aviator函数。
     *
     * @param aviatorFunction Aviator函数
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AviatorFunction aviatorFunction) {
        return aviatorFunctionService.updateById(aviatorFunction);
    }

    /**
     * 查询所有Aviator函数。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AviatorFunction> list() {
        return aviatorFunctionService.list();
    }

    /**
     * 根据Aviator函数主键获取详细信息。
     *
     * @param id Aviator函数主键
     * @return Aviator函数详情
     */
    @GetMapping("getInfo/{id}")
    public AviatorFunction getInfo(@PathVariable Serializable id) {
        return aviatorFunctionService.getById(id);
    }

    /**
     * 分页查询Aviator函数。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AviatorFunction> page(Page<AviatorFunction> page) {
        return aviatorFunctionService.page(page);
    }

}