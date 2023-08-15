package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.DeriveModelTimeColumnRelation;
import com.yanggu.metric_calculate.config.service.DeriveModelTimeColumnRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private DeriveModelTimeColumnRelationService deriveTimeColumnRelationService;

    /**
     * 添加派生指标和时间字段中间表。
     *
     * @param deriveModelTimeColumnRelation 派生指标和时间字段中间表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody DeriveModelTimeColumnRelation deriveModelTimeColumnRelation) {
        return deriveTimeColumnRelationService.save(deriveModelTimeColumnRelation);
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
     * @param deriveModelTimeColumnRelation 派生指标和时间字段中间表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody DeriveModelTimeColumnRelation deriveModelTimeColumnRelation) {
        return deriveTimeColumnRelationService.updateById(deriveModelTimeColumnRelation);
    }

    /**
     * 查询所有派生指标和时间字段中间表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<DeriveModelTimeColumnRelation> list() {
        return deriveTimeColumnRelationService.list();
    }

    /**
     * 根据派生指标和时间字段中间表主键获取详细信息。
     *
     * @param id 派生指标和时间字段中间表主键
     * @return 派生指标和时间字段中间表详情
     */
    @GetMapping("getInfo/{id}")
    public DeriveModelTimeColumnRelation getInfo(@PathVariable Serializable id) {
        return deriveTimeColumnRelationService.getById(id);
    }

    /**
     * 分页查询派生指标和时间字段中间表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<DeriveModelTimeColumnRelation> page(Page<DeriveModelTimeColumnRelation> page) {
        return deriveTimeColumnRelationService.page(page);
    }

}