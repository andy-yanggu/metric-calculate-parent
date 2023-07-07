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
import com.yanggu.metric_calculate.config.entity.BaseUdafParamObjectiveCompareFieldExpressListRelation;
import com.yanggu.metric_calculate.config.service.BaseUdafParamObjectiveCompareFieldExpressListRelationService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 基本聚合参数，对象型比较字段列表中间表 控制层。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/baseUdafParamObjectiveCompareFieldExpressListRelation")
public class BaseUdafParamObjectiveCompareFieldExpressListRelationController {

    @Autowired
    private BaseUdafParamObjectiveCompareFieldExpressListRelationService baseUdafParamObjectiveCompareFieldExpressListRelationService;

    /**
     * 添加基本聚合参数，对象型比较字段列表中间表。
     *
     * @param baseUdafParamObjectiveCompareFieldExpressListRelation 基本聚合参数，对象型比较字段列表中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody BaseUdafParamObjectiveCompareFieldExpressListRelation baseUdafParamObjectiveCompareFieldExpressListRelation) {
        return baseUdafParamObjectiveCompareFieldExpressListRelationService.save(baseUdafParamObjectiveCompareFieldExpressListRelation);
    }

    /**
     * 根据主键删除基本聚合参数，对象型比较字段列表中间表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return baseUdafParamObjectiveCompareFieldExpressListRelationService.removeById(id);
    }

    /**
     * 根据主键更新基本聚合参数，对象型比较字段列表中间表。
     *
     * @param baseUdafParamObjectiveCompareFieldExpressListRelation 基本聚合参数，对象型比较字段列表中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody BaseUdafParamObjectiveCompareFieldExpressListRelation baseUdafParamObjectiveCompareFieldExpressListRelation) {
        return baseUdafParamObjectiveCompareFieldExpressListRelationService.updateById(baseUdafParamObjectiveCompareFieldExpressListRelation);
    }

    /**
     * 查询所有基本聚合参数，对象型比较字段列表中间表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<BaseUdafParamObjectiveCompareFieldExpressListRelation> list() {
        return baseUdafParamObjectiveCompareFieldExpressListRelationService.list();
    }

    /**
     * 根据基本聚合参数，对象型比较字段列表中间表主键获取详细信息。
     *
     * @param id 基本聚合参数，对象型比较字段列表中间表主键
     * @return 基本聚合参数，对象型比较字段列表中间表详情
     */
    @GetMapping("getInfo/{id}")
    public BaseUdafParamObjectiveCompareFieldExpressListRelation getInfo(@PathVariable Serializable id) {
        return baseUdafParamObjectiveCompareFieldExpressListRelationService.getById(id);
    }

    /**
     * 分页查询基本聚合参数，对象型比较字段列表中间表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<BaseUdafParamObjectiveCompareFieldExpressListRelation> page(Page<BaseUdafParamObjectiveCompareFieldExpressListRelation> page) {
        return baseUdafParamObjectiveCompareFieldExpressListRelationService.page(page);
    }

}