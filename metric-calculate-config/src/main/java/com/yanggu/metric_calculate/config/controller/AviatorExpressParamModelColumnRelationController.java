package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParamModelColumnRelation;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamModelColumnRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Aviator表达式和宽表字段中间表 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/aviatorExpressParamModelColumnRelation")
public class AviatorExpressParamModelColumnRelationController {

    @Autowired
    private AviatorExpressParamModelColumnRelationService aviatorExpressParamModelColumnRelationService;

    /**
     * 添加 Aviator表达式和宽表字段中间表
     *
     * @param aviatorExpressParamModelColumnRelation Aviator表达式和宽表字段中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    public boolean save(@RequestBody AviatorExpressParamModelColumnRelation aviatorExpressParamModelColumnRelation) {
        return aviatorExpressParamModelColumnRelationService.save(aviatorExpressParamModelColumnRelation);
    }


    /**
     * 根据主键删除Aviator表达式和宽表字段中间表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aviatorExpressParamModelColumnRelationService.removeById(id);
    }


    /**
     * 根据主键更新Aviator表达式和宽表字段中间表
     *
     * @param aviatorExpressParamModelColumnRelation Aviator表达式和宽表字段中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    public boolean update(@RequestBody AviatorExpressParamModelColumnRelation aviatorExpressParamModelColumnRelation) {
        return aviatorExpressParamModelColumnRelationService.updateById(aviatorExpressParamModelColumnRelation);
    }


    /**
     * 查询所有Aviator表达式和宽表字段中间表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    public List<AviatorExpressParamModelColumnRelation> list() {
        return aviatorExpressParamModelColumnRelationService.list();
    }


    /**
     * 根据Aviator表达式和宽表字段中间表主键获取详细信息。
     *
     * @param id aviatorExpressParamModelColumnRelation主键
     * @return Aviator表达式和宽表字段中间表详情
     */
    @GetMapping("/getInfo/{id}")
    public AviatorExpressParamModelColumnRelation getInfo(@PathVariable Serializable id) {
        return aviatorExpressParamModelColumnRelationService.getById(id);
    }


    /**
     * 分页查询Aviator表达式和宽表字段中间表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public Page<AviatorExpressParamModelColumnRelation> page(Page<AviatorExpressParamModelColumnRelation> page) {
        return aviatorExpressParamModelColumnRelationService.page(page);
    }
}