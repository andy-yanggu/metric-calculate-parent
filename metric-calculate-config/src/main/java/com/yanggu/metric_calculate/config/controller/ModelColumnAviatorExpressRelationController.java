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
import com.yanggu.metric_calculate.config.entity.ModelColumnAviatorExpressRelation;
import com.yanggu.metric_calculate.config.service.ModelColumnAviatorExpressRelationService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 宽表字段表达式关系表 控制层。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/modelColumnAviatorExpressRelation")
public class ModelColumnAviatorExpressRelationController {

    @Autowired
    private ModelColumnAviatorExpressRelationService modelColumnAviatorExpressRelationService;

    /**
     * 添加宽表字段表达式关系表。
     *
     * @param modelColumnAviatorExpressRelation 宽表字段表达式关系表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ModelColumnAviatorExpressRelation modelColumnAviatorExpressRelation) {
        return modelColumnAviatorExpressRelationService.save(modelColumnAviatorExpressRelation);
    }

    /**
     * 根据主键删除宽表字段表达式关系表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return modelColumnAviatorExpressRelationService.removeById(id);
    }

    /**
     * 根据主键更新宽表字段表达式关系表。
     *
     * @param modelColumnAviatorExpressRelation 宽表字段表达式关系表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ModelColumnAviatorExpressRelation modelColumnAviatorExpressRelation) {
        return modelColumnAviatorExpressRelationService.updateById(modelColumnAviatorExpressRelation);
    }

    /**
     * 查询所有宽表字段表达式关系表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ModelColumnAviatorExpressRelation> list() {
        return modelColumnAviatorExpressRelationService.list();
    }

    /**
     * 根据宽表字段表达式关系表主键获取详细信息。
     *
     * @param id 宽表字段表达式关系表主键
     * @return 宽表字段表达式关系表详情
     */
    @GetMapping("getInfo/{id}")
    public ModelColumnAviatorExpressRelation getInfo(@PathVariable Serializable id) {
        return modelColumnAviatorExpressRelationService.getById(id);
    }

    /**
     * 分页查询宽表字段表达式关系表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ModelColumnAviatorExpressRelation> page(Page<ModelColumnAviatorExpressRelation> page) {
        return modelColumnAviatorExpressRelationService.page(page);
    }

}