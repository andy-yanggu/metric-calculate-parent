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
import com.yanggu.metric_calculate.config.pojo.entity.BaseUdafParam;
import com.yanggu.metric_calculate.config.service.BaseUdafParamService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 数值型、集合型、对象型聚合函数相关参数 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/baseUdafParam")
public class BaseUdafParamController {

    @Autowired
    private BaseUdafParamService baseUdafParamService;

    /**
     * 添加数值型、集合型、对象型聚合函数相关参数。
     *
     * @param baseUdafParam 数值型、集合型、对象型聚合函数相关参数
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody BaseUdafParam baseUdafParam) {
        return baseUdafParamService.save(baseUdafParam);
    }

    /**
     * 根据主键删除数值型、集合型、对象型聚合函数相关参数。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return baseUdafParamService.removeById(id);
    }

    /**
     * 根据主键更新数值型、集合型、对象型聚合函数相关参数。
     *
     * @param baseUdafParam 数值型、集合型、对象型聚合函数相关参数
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody BaseUdafParam baseUdafParam) {
        return baseUdafParamService.updateById(baseUdafParam);
    }

    /**
     * 查询所有数值型、集合型、对象型聚合函数相关参数。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<BaseUdafParam> list() {
        return baseUdafParamService.list();
    }

    /**
     * 根据数值型、集合型、对象型聚合函数相关参数主键获取详细信息。
     *
     * @param id 数值型、集合型、对象型聚合函数相关参数主键
     * @return 数值型、集合型、对象型聚合函数相关参数详情
     */
    @GetMapping("getInfo/{id}")
    public BaseUdafParam getInfo(@PathVariable Serializable id) {
        return baseUdafParamService.getById(id);
    }

    /**
     * 分页查询数值型、集合型、对象型聚合函数相关参数。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<BaseUdafParam> page(Page<BaseUdafParam> page) {
        return baseUdafParamService.page(page);
    }

}