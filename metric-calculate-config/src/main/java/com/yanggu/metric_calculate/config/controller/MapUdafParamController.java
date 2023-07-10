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
import com.yanggu.metric_calculate.config.pojo.entity.MapUdafParam;
import com.yanggu.metric_calculate.config.service.MapUdafParamService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 映射类型udaf参数 控制层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@RestController
@RequestMapping("/mapUdafParam")
public class MapUdafParamController {

    @Autowired
    private MapUdafParamService mapUdafParamService;

    /**
     * 添加映射类型udaf参数。
     *
     * @param mapUdafParam 映射类型udaf参数
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody MapUdafParam mapUdafParam) {
        return mapUdafParamService.save(mapUdafParam);
    }

    /**
     * 根据主键删除映射类型udaf参数。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return mapUdafParamService.removeById(id);
    }

    /**
     * 根据主键更新映射类型udaf参数。
     *
     * @param mapUdafParam 映射类型udaf参数
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody MapUdafParam mapUdafParam) {
        return mapUdafParamService.updateById(mapUdafParam);
    }

    /**
     * 查询所有映射类型udaf参数。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<MapUdafParam> list() {
        return mapUdafParamService.list();
    }

    /**
     * 根据映射类型udaf参数主键获取详细信息。
     *
     * @param id 映射类型udaf参数主键
     * @return 映射类型udaf参数详情
     */
    @GetMapping("getInfo/{id}")
    public MapUdafParam getInfo(@PathVariable Serializable id) {
        return mapUdafParamService.getById(id);
    }

    /**
     * 分页查询映射类型udaf参数。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<MapUdafParam> page(Page<MapUdafParam> page) {
        return mapUdafParamService.page(page);
    }

}