package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParamAviatorFunctionInstanceRelation;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamAviatorFunctionInstanceRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Aviator函数和Aviator函数实例中间表 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/aviatorExpressParamAviatorFunctionInstanceRelation")
public class AviatorExpressParamAviatorFunctionInstanceRelationController {

    @Autowired
    private AviatorExpressParamAviatorFunctionInstanceRelationService aviatorExpressParamAviatorFunctionInstanceRelationService;

    /**
     * 添加 Aviator函数和Aviator函数实例中间表
     *
     * @param aviatorExpressParamAviatorFunctionInstanceRelation Aviator函数和Aviator函数实例中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    public boolean save(@RequestBody AviatorExpressParamAviatorFunctionInstanceRelation aviatorExpressParamAviatorFunctionInstanceRelation) {
        return aviatorExpressParamAviatorFunctionInstanceRelationService.save(aviatorExpressParamAviatorFunctionInstanceRelation);
    }


    /**
     * 根据主键删除Aviator函数和Aviator函数实例中间表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aviatorExpressParamAviatorFunctionInstanceRelationService.removeById(id);
    }


    /**
     * 根据主键更新Aviator函数和Aviator函数实例中间表
     *
     * @param aviatorExpressParamAviatorFunctionInstanceRelation Aviator函数和Aviator函数实例中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    public boolean update(@RequestBody AviatorExpressParamAviatorFunctionInstanceRelation aviatorExpressParamAviatorFunctionInstanceRelation) {
        return aviatorExpressParamAviatorFunctionInstanceRelationService.updateById(aviatorExpressParamAviatorFunctionInstanceRelation);
    }


    /**
     * 查询所有Aviator函数和Aviator函数实例中间表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    public List<AviatorExpressParamAviatorFunctionInstanceRelation> list() {
        return aviatorExpressParamAviatorFunctionInstanceRelationService.list();
    }


    /**
     * 根据Aviator函数和Aviator函数实例中间表主键获取详细信息。
     *
     * @param id aviatorExpressParamAviatorFunctionInstanceRelation主键
     * @return Aviator函数和Aviator函数实例中间表详情
     */
    @GetMapping("/getInfo/{id}")
    public AviatorExpressParamAviatorFunctionInstanceRelation getInfo(@PathVariable Serializable id) {
        return aviatorExpressParamAviatorFunctionInstanceRelationService.getById(id);
    }


    /**
     * 分页查询Aviator函数和Aviator函数实例中间表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public Page<AviatorExpressParamAviatorFunctionInstanceRelation> page(Page<AviatorExpressParamAviatorFunctionInstanceRelation> page) {
        return aviatorExpressParamAviatorFunctionInstanceRelationService.page(page);
    }
}