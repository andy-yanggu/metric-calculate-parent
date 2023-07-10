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
import com.yanggu.metric_calculate.config.pojo.entity.DeriveTimeColumnRelation;
import com.yanggu.metric_calculate.config.service.DeriveTimeColumnRelationService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 派生指标和时间字段中间表 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/deriveTimeColumnRelation")
public class DeriveTimeColumnRelationController {

    @Autowired
    private DeriveTimeColumnRelationService deriveTimeColumnRelationService;

    /**
     * 添加派生指标和时间字段中间表。
     *
     * @param deriveTimeColumnRelation 派生指标和时间字段中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody DeriveTimeColumnRelation deriveTimeColumnRelation) {
        return deriveTimeColumnRelationService.save(deriveTimeColumnRelation);
    }

    /**
     * 根据主键删除派生指标和时间字段中间表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return deriveTimeColumnRelationService.removeById(id);
    }

    /**
     * 根据主键更新派生指标和时间字段中间表。
     *
     * @param deriveTimeColumnRelation 派生指标和时间字段中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody DeriveTimeColumnRelation deriveTimeColumnRelation) {
        return deriveTimeColumnRelationService.updateById(deriveTimeColumnRelation);
    }

    /**
     * 查询所有派生指标和时间字段中间表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<DeriveTimeColumnRelation> list() {
        return deriveTimeColumnRelationService.list();
    }

    /**
     * 根据派生指标和时间字段中间表主键获取详细信息。
     *
     * @param id 派生指标和时间字段中间表主键
     * @return 派生指标和时间字段中间表详情
     */
    @GetMapping("getInfo/{id}")
    public DeriveTimeColumnRelation getInfo(@PathVariable Serializable id) {
        return deriveTimeColumnRelationService.getById(id);
    }

    /**
     * 分页查询派生指标和时间字段中间表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<DeriveTimeColumnRelation> page(Page<DeriveTimeColumnRelation> page) {
        return deriveTimeColumnRelationService.page(page);
    }

}