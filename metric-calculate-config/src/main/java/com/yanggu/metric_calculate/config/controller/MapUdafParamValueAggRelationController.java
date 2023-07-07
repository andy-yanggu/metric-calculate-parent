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
import com.yanggu.metric_calculate.config.entity.MapUdafParamValueAggRelation;
import com.yanggu.metric_calculate.config.service.MapUdafParamValueAggRelationService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表 控制层。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/mapUdafParamValueAggRelation")
public class MapUdafParamValueAggRelationController {

    @Autowired
    private MapUdafParamValueAggRelationService mapUdafParamValueAggRelationService;

    /**
     * 添加映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表。
     *
     * @param mapUdafParamValueAggRelation 映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody MapUdafParamValueAggRelation mapUdafParamValueAggRelation) {
        return mapUdafParamValueAggRelationService.save(mapUdafParamValueAggRelation);
    }

    /**
     * 根据主键删除映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return mapUdafParamValueAggRelationService.removeById(id);
    }

    /**
     * 根据主键更新映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表。
     *
     * @param mapUdafParamValueAggRelation 映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody MapUdafParamValueAggRelation mapUdafParamValueAggRelation) {
        return mapUdafParamValueAggRelationService.updateById(mapUdafParamValueAggRelation);
    }

    /**
     * 查询所有映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<MapUdafParamValueAggRelation> list() {
        return mapUdafParamValueAggRelationService.list();
    }

    /**
     * 根据映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表主键获取详细信息。
     *
     * @param id 映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表主键
     * @return 映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表详情
     */
    @GetMapping("getInfo/{id}")
    public MapUdafParamValueAggRelation getInfo(@PathVariable Serializable id) {
        return mapUdafParamValueAggRelationService.getById(id);
    }

    /**
     * 分页查询映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<MapUdafParamValueAggRelation> page(Page<MapUdafParamValueAggRelation> page) {
        return mapUdafParamValueAggRelationService.page(page);
    }

}