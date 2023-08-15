package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamMixAggMapRelation;
import com.yanggu.metric_calculate.config.service.MixUdafParamMixAggMapRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/mixUdafParamMixAggMapRelation")
public class MixUdafParamMixAggMapRelationController {

    @Autowired
    private MixUdafParamMixAggMapRelationService mixUdafParamMixAggMapRelationService;

    /**
     * 添加混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表。
     *
     * @param mixUdafParamMixAggMapRelation 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody MixUdafParamMixAggMapRelation mixUdafParamMixAggMapRelation) {
        return mixUdafParamMixAggMapRelationService.save(mixUdafParamMixAggMapRelation);
    }

    /**
     * 根据主键删除混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return mixUdafParamMixAggMapRelationService.removeById(id);
    }

    /**
     * 根据主键更新混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表。
     *
     * @param mixUdafParamMixAggMapRelation 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody MixUdafParamMixAggMapRelation mixUdafParamMixAggMapRelation) {
        return mixUdafParamMixAggMapRelationService.updateById(mixUdafParamMixAggMapRelation);
    }

    /**
     * 查询所有混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<MixUdafParamMixAggMapRelation> list() {
        return mixUdafParamMixAggMapRelationService.list();
    }

    /**
     * 根据混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表主键获取详细信息。
     *
     * @param id 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表主键
     * @return 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表详情
     */
    @GetMapping("getInfo/{id}")
    public MixUdafParamMixAggMapRelation getInfo(@PathVariable Serializable id) {
        return mixUdafParamMixAggMapRelationService.getById(id);
    }

    /**
     * 分页查询混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<MixUdafParamMixAggMapRelation> page(Page<MixUdafParamMixAggMapRelation> page) {
        return mixUdafParamMixAggMapRelationService.page(page);
    }

}