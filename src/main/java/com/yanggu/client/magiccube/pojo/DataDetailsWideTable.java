package com.yanggu.client.magiccube.pojo;

import lombok.Data;

import java.util.List;


/**
 * 数据明细宽表
 */
@Data
public class DataDetailsWideTable {

    /**
     * 数据明细宽表id
     */
    private Long id;

    /**
     * 宽表名称
     */
    private String name;

    /**
     * 中文名
     */
    private String displayName;

    /**
     * 原子指标
     */
    private List<Atom> atom;

    /**
     * 派生指标
     */
    private List<Derive> derive;

    /**
     * 复合指标
     */
    private List<Composite> composite;

    /**
     * 全局指标
     */
    private List<Global> global;

    /**
     * 宽表字段
     */
    private List<Fields> fields;

}
