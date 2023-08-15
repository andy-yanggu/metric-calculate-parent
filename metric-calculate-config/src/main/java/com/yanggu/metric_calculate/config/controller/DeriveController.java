package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import com.yanggu.metric_calculate.config.service.DeriveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 派生指标
 */
@RestController
@Tag(name = "派生指标")
@RequestMapping("/derive")
public class DeriveController {

    @Autowired
    private DeriveService deriveService;

    @PostMapping("save")
    @Operation(summary = "新增派生指标")
    public void save(@RequestBody DeriveDto derive) {
        deriveService.create(derive);
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
    public DeriveDto getInfo(@PathVariable Integer id) {
        return deriveService.queryById(id);
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