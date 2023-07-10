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
import com.yanggu.metric_calculate.config.entity.MixUdafParam;
import com.yanggu.metric_calculate.config.service.MixUdafParamService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 混合类型udaf参数 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/mixUdafParam")
public class MixUdafParamController {

    @Autowired
    private MixUdafParamService mixUdafParamService;

    /**
     * 添加混合类型udaf参数。
     *
     * @param mixUdafParam 混合类型udaf参数
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody MixUdafParam mixUdafParam) {
        return mixUdafParamService.save(mixUdafParam);
    }

    /**
     * 根据主键删除混合类型udaf参数。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return mixUdafParamService.removeById(id);
    }

    /**
     * 根据主键更新混合类型udaf参数。
     *
     * @param mixUdafParam 混合类型udaf参数
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody MixUdafParam mixUdafParam) {
        return mixUdafParamService.updateById(mixUdafParam);
    }

    /**
     * 查询所有混合类型udaf参数。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<MixUdafParam> list() {
        return mixUdafParamService.list();
    }

    /**
     * 根据混合类型udaf参数主键获取详细信息。
     *
     * @param id 混合类型udaf参数主键
     * @return 混合类型udaf参数详情
     */
    @GetMapping("getInfo/{id}")
    public MixUdafParam getInfo(@PathVariable Serializable id) {
        return mixUdafParamService.getById(id);
    }

    /**
     * 分页查询混合类型udaf参数。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<MixUdafParam> page(Page<MixUdafParam> page) {
        return mixUdafParamService.page(page);
    }

}