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
import com.yanggu.metric_calculate.config.pojo.entity.FieldOrderParam;
import com.yanggu.metric_calculate.config.service.FieldOrderParamService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 字段排序配置类 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/fieldOrderParam")
public class FieldOrderParamController {

    @Autowired
    private FieldOrderParamService fieldOrderParamService;

    /**
     * 添加字段排序配置类。
     *
     * @param fieldOrderParam 字段排序配置类
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody FieldOrderParam fieldOrderParam) {
        return fieldOrderParamService.save(fieldOrderParam);
    }

    /**
     * 根据主键删除字段排序配置类。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return fieldOrderParamService.removeById(id);
    }

    /**
     * 根据主键更新字段排序配置类。
     *
     * @param fieldOrderParam 字段排序配置类
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody FieldOrderParam fieldOrderParam) {
        return fieldOrderParamService.updateById(fieldOrderParam);
    }

    /**
     * 查询所有字段排序配置类。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<FieldOrderParam> list() {
        return fieldOrderParamService.list();
    }

    /**
     * 根据字段排序配置类主键获取详细信息。
     *
     * @param id 字段排序配置类主键
     * @return 字段排序配置类详情
     */
    @GetMapping("getInfo/{id}")
    public FieldOrderParam getInfo(@PathVariable Serializable id) {
        return fieldOrderParamService.getById(id);
    }

    /**
     * 分页查询字段排序配置类。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<FieldOrderParam> page(Page<FieldOrderParam> page) {
        return fieldOrderParamService.page(page);
    }

}