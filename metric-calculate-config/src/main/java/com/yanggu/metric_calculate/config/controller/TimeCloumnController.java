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
import com.yanggu.metric_calculate.config.entity.TimeCloumn;
import com.yanggu.metric_calculate.config.service.TimeCloumnService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 时间字段 控制层。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/timeCloumn")
public class TimeCloumnController {

    @Autowired
    private TimeCloumnService timeCloumnService;

    /**
     * 添加时间字段。
     *
     * @param timeCloumn 时间字段
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody TimeCloumn timeCloumn) {
        return timeCloumnService.save(timeCloumn);
    }

    /**
     * 根据主键删除时间字段。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return timeCloumnService.removeById(id);
    }

    /**
     * 根据主键更新时间字段。
     *
     * @param timeCloumn 时间字段
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody TimeCloumn timeCloumn) {
        return timeCloumnService.updateById(timeCloumn);
    }

    /**
     * 查询所有时间字段。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<TimeCloumn> list() {
        return timeCloumnService.list();
    }

    /**
     * 根据时间字段主键获取详细信息。
     *
     * @param id 时间字段主键
     * @return 时间字段详情
     */
    @GetMapping("getInfo/{id}")
    public TimeCloumn getInfo(@PathVariable Serializable id) {
        return timeCloumnService.getById(id);
    }

    /**
     * 分页查询时间字段。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<TimeCloumn> page(Page<TimeCloumn> page) {
        return timeCloumnService.page(page);
    }

}