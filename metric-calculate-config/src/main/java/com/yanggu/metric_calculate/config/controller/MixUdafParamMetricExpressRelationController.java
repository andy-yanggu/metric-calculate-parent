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
import com.yanggu.metric_calculate.config.entity.MixUdafParamMetricExpressRelation;
import com.yanggu.metric_calculate.config.service.MixUdafParamMetricExpressRelationService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 混合聚合参数，多个聚合值的计算表达式中间表 控制层。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/mixUdafParamMetricExpressRelation")
public class MixUdafParamMetricExpressRelationController {

    @Autowired
    private MixUdafParamMetricExpressRelationService mixUdafParamMetricExpressRelationService;

    /**
     * 添加混合聚合参数，多个聚合值的计算表达式中间表。
     *
     * @param mixUdafParamMetricExpressRelation 混合聚合参数，多个聚合值的计算表达式中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody MixUdafParamMetricExpressRelation mixUdafParamMetricExpressRelation) {
        return mixUdafParamMetricExpressRelationService.save(mixUdafParamMetricExpressRelation);
    }

    /**
     * 根据主键删除混合聚合参数，多个聚合值的计算表达式中间表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return mixUdafParamMetricExpressRelationService.removeById(id);
    }

    /**
     * 根据主键更新混合聚合参数，多个聚合值的计算表达式中间表。
     *
     * @param mixUdafParamMetricExpressRelation 混合聚合参数，多个聚合值的计算表达式中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody MixUdafParamMetricExpressRelation mixUdafParamMetricExpressRelation) {
        return mixUdafParamMetricExpressRelationService.updateById(mixUdafParamMetricExpressRelation);
    }

    /**
     * 查询所有混合聚合参数，多个聚合值的计算表达式中间表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<MixUdafParamMetricExpressRelation> list() {
        return mixUdafParamMetricExpressRelationService.list();
    }

    /**
     * 根据混合聚合参数，多个聚合值的计算表达式中间表主键获取详细信息。
     *
     * @param id 混合聚合参数，多个聚合值的计算表达式中间表主键
     * @return 混合聚合参数，多个聚合值的计算表达式中间表详情
     */
    @GetMapping("getInfo/{id}")
    public MixUdafParamMetricExpressRelation getInfo(@PathVariable Serializable id) {
        return mixUdafParamMetricExpressRelationService.getById(id);
    }

    /**
     * 分页查询混合聚合参数，多个聚合值的计算表达式中间表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<MixUdafParamMetricExpressRelation> page(Page<MixUdafParamMetricExpressRelation> page) {
        return mixUdafParamMetricExpressRelationService.page(page);
    }

}