package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.entity.AviatorExpressParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * Aviator表达式配置 控制层。
 *
 * @author MondayLi
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/aviatorExpressParam")
public class AviatorExpressParamController {

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    /**
     * 添加Aviator表达式配置。
     *
     * @param aviatorExpressParam Aviator表达式配置
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AviatorExpressParam aviatorExpressParam) {
        return aviatorExpressParamService.save(aviatorExpressParam);
    }

    /**
     * 根据主键删除Aviator表达式配置。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return aviatorExpressParamService.removeById(id);
    }

    /**
     * 根据主键更新Aviator表达式配置。
     *
     * @param aviatorExpressParam Aviator表达式配置
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AviatorExpressParam aviatorExpressParam) {
        return aviatorExpressParamService.updateById(aviatorExpressParam);
    }

    /**
     * 查询所有Aviator表达式配置。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AviatorExpressParam> list() {
        return aviatorExpressParamService.list();
    }

    /**
     * 根据Aviator表达式配置主键获取详细信息。
     *
     * @param id Aviator表达式配置主键
     * @return Aviator表达式配置详情
     */
    @GetMapping("getInfo/{id}")
    public AviatorExpressParam getInfo(@PathVariable Serializable id) {
        return aviatorExpressParamService.getById(id);
    }

    /**
     * 分页查询Aviator表达式配置。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AviatorExpressParam> page(Page<AviatorExpressParam> page) {
        return aviatorExpressParamService.page(page);
    }

}