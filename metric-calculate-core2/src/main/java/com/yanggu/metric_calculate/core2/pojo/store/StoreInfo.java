package com.yanggu.metric_calculate.core2.pojo.store;

import lombok.Data;

import java.util.List;

/**
 * 存储相关信息
 */
@Data
public class StoreInfo {

    /**
     * 是否存储, true存储、false不存储
     */
    private Boolean isStore;

    /**
     * 存储宽表
     */
    private List<StoreTable> storeTableList;

}
