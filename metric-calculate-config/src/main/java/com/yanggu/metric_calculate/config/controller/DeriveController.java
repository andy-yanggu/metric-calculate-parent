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
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import com.yanggu.metric_calculate.config.service.DeriveService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 派生指标 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/derive")
public class DeriveController {

    @Autowired
    private DeriveService deriveService;

    /**
     * 添加派生指标。
     *
     * @param derive 派生指标
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Derive derive) {
        return deriveService.save(derive);
    }

    /**
     * 根据主键删除派生指标。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return deriveService.removeById(id);
    }

    /**
     * 根据主键更新派生指标。
     *
     * @param derive 派生指标
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Derive derive) {
        return deriveService.updateById(derive);
    }

    /**
     * 查询所有派生指标。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<Derive> list() {
        return deriveService.list();
    }

    /**
     * 根据派生指标主键获取详细信息。
     *
     * @param id 派生指标主键
     * @return 派生指标详情
     */
    @GetMapping("getInfo/{id}")
    public Derive getInfo(@PathVariable Long id) {
        return deriveService.getById(id);
    }

    /**
     * 分页查询派生指标。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<Derive> page(Page<Derive> page) {
        return deriveService.page(page);
    }

}