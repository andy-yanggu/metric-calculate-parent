package com.yanggu.metric_calculate.config.controller;

import com.mybatisflex.core.paginate.Page;
import com.yanggu.metric_calculate.config.pojo.entity.WindowParam;
import com.yanggu.metric_calculate.config.service.WindowParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 窗口相关参数 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/windowParam")
public class WindowParamController {

    @Autowired
    private WindowParamService windowParamService;

    /**
     * 添加窗口相关参数。
     *
     * @param windowParam 窗口相关参数
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody WindowParam windowParam) {
        return windowParamService.save(windowParam);
    }

    /**
     * 根据主键删除窗口相关参数。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return windowParamService.removeById(id);
    }

    /**
     * 根据主键更新窗口相关参数。
     *
     * @param windowParam 窗口相关参数
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody WindowParam windowParam) {
        return windowParamService.updateById(windowParam);
    }

    /**
     * 查询所有窗口相关参数。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<WindowParam> list() {
        return windowParamService.list();
    }

    /**
     * 根据窗口相关参数主键获取详细信息。
     *
     * @param id 窗口相关参数主键
     * @return 窗口相关参数详情
     */
    @GetMapping("getInfo/{id}")
    public WindowParam getInfo(@PathVariable Serializable id) {
        return windowParamService.getById(id);
    }

    /**
     * 分页查询窗口相关参数。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<WindowParam> page(Page<WindowParam> page) {
        return windowParamService.page(page);
    }

}