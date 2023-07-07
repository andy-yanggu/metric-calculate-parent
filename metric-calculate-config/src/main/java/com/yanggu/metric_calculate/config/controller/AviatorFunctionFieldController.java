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
import com.yanggu.metric_calculate.config.service.AviatorFunctionFieldService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * Aviator函数字段模板 控制层。
 *
 * @author MondayLi
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/aviatorFunctionField")
public class AviatorFunctionFieldController {

    @Autowired
    private AviatorFunctionFieldService aviatorFunctionFieldService;

    /**
     * 添加Aviator函数字段模板。
     *
     * @param aviatorFunctionField Aviator函数字段模板
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AviatorFunctionField aviatorFunctionField) {
        return aviatorFunctionFieldService.save(aviatorFunctionField);
    }

    /**
     * 根据主键删除Aviator函数字段模板。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aviatorFunctionFieldService.removeById(id);
    }

    /**
     * 根据主键更新Aviator函数字段模板。
     *
     * @param aviatorFunctionField Aviator函数字段模板
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AviatorFunctionField aviatorFunctionField) {
        return aviatorFunctionFieldService.updateById(aviatorFunctionField);
    }

    /**
     * 查询所有Aviator函数字段模板。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AviatorFunctionField> list() {
        return aviatorFunctionFieldService.list();
    }

    /**
     * 根据Aviator函数字段模板主键获取详细信息。
     *
     * @param id Aviator函数字段模板主键
     * @return Aviator函数字段模板详情
     */
    @GetMapping("getInfo/{id}")
    public AviatorFunctionField getInfo(@PathVariable Serializable id) {
        return aviatorFunctionFieldService.getById(id);
    }

    /**
     * 分页查询Aviator函数字段模板。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AviatorFunctionField> page(Page<AviatorFunctionField> page) {
        return aviatorFunctionFieldService.page(page);
    }

}