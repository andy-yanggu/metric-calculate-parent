package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.BaseUdafParamMetricExpressRelation;
import com.yanggu.metric_calculate.config.service.BaseUdafParamMetricExpressRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 基本聚合参数，度量字段表达式中间表 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/baseUdafParamMetricExpressRelation")
public class BaseUdafParamMetricExpressRelationController {

    @Autowired
    private BaseUdafParamMetricExpressRelationService baseUdafParamMetricExpressRelationService;

    /**
     * 添加基本聚合参数，度量字段表达式中间表。
     *
     * @param baseUdafParamMetricExpressRelation 基本聚合参数，度量字段表达式中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody BaseUdafParamMetricExpressRelation baseUdafParamMetricExpressRelation) {
        return baseUdafParamMetricExpressRelationService.save(baseUdafParamMetricExpressRelation);
    }

    /**
     * 根据主键删除基本聚合参数，度量字段表达式中间表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return baseUdafParamMetricExpressRelationService.removeById(id);
    }

    /**
     * 根据主键更新基本聚合参数，度量字段表达式中间表。
     *
     * @param baseUdafParamMetricExpressRelation 基本聚合参数，度量字段表达式中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody BaseUdafParamMetricExpressRelation baseUdafParamMetricExpressRelation) {
        return baseUdafParamMetricExpressRelationService.updateById(baseUdafParamMetricExpressRelation);
    }

    /**
     * 查询所有基本聚合参数，度量字段表达式中间表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<BaseUdafParamMetricExpressRelation> list() {
        return baseUdafParamMetricExpressRelationService.list();
    }

    /**
     * 根据基本聚合参数，度量字段表达式中间表主键获取详细信息。
     *
     * @param id 基本聚合参数，度量字段表达式中间表主键
     * @return 基本聚合参数，度量字段表达式中间表详情
     */
    @GetMapping("getInfo/{id}")
    public BaseUdafParamMetricExpressRelation getInfo(@PathVariable Serializable id) {
        return baseUdafParamMetricExpressRelationService.getById(id);
    }

    /**
     * 分页查询基本聚合参数，度量字段表达式中间表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<BaseUdafParamMetricExpressRelation> page(Page<BaseUdafParamMetricExpressRelation> page) {
        return baseUdafParamMetricExpressRelationService.page(page);
    }

}