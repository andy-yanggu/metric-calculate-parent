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
import com.yanggu.metric_calculate.config.entity.BaseUdafParamDistinctFieldListRelation;
import com.yanggu.metric_calculate.config.service.BaseUdafParamDistinctFieldListRelationService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 基本聚合参数，去重字段列表中间表 控制层。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/baseUdafParamDistinctFieldListRelation")
public class BaseUdafParamDistinctFieldListRelationController {

    @Autowired
    private BaseUdafParamDistinctFieldListRelationService baseUdafParamDistinctFieldListRelationService;

    /**
     * 添加基本聚合参数，去重字段列表中间表。
     *
     * @param baseUdafParamDistinctFieldListRelation 基本聚合参数，去重字段列表中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody BaseUdafParamDistinctFieldListRelation baseUdafParamDistinctFieldListRelation) {
        return baseUdafParamDistinctFieldListRelationService.save(baseUdafParamDistinctFieldListRelation);
    }

    /**
     * 根据主键删除基本聚合参数，去重字段列表中间表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return baseUdafParamDistinctFieldListRelationService.removeById(id);
    }

    /**
     * 根据主键更新基本聚合参数，去重字段列表中间表。
     *
     * @param baseUdafParamDistinctFieldListRelation 基本聚合参数，去重字段列表中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody BaseUdafParamDistinctFieldListRelation baseUdafParamDistinctFieldListRelation) {
        return baseUdafParamDistinctFieldListRelationService.updateById(baseUdafParamDistinctFieldListRelation);
    }

    /**
     * 查询所有基本聚合参数，去重字段列表中间表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<BaseUdafParamDistinctFieldListRelation> list() {
        return baseUdafParamDistinctFieldListRelationService.list();
    }

    /**
     * 根据基本聚合参数，去重字段列表中间表主键获取详细信息。
     *
     * @param id 基本聚合参数，去重字段列表中间表主键
     * @return 基本聚合参数，去重字段列表中间表详情
     */
    @GetMapping("getInfo/{id}")
    public BaseUdafParamDistinctFieldListRelation getInfo(@PathVariable Serializable id) {
        return baseUdafParamDistinctFieldListRelationService.getById(id);
    }

    /**
     * 分页查询基本聚合参数，去重字段列表中间表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<BaseUdafParamDistinctFieldListRelation> page(Page<BaseUdafParamDistinctFieldListRelation> page) {
        return baseUdafParamDistinctFieldListRelationService.page(page);
    }

}