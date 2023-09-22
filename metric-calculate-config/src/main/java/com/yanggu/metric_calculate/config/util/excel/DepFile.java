/*
 * Copyright 2020, Zetyun DEP All rights reserved.
 */

package com.yanggu.metric_calculate.config.util.excel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DepFile.
 *
 * @author admin
 * @date 2020-12-10
 */
@EqualsAndHashCode
@Data
public class DepFile {
    private static final long serialVersionUID = 1L;

    /**
     * id.
     */
    private Integer id;

    /**
     * file.
     */
    private byte[] file;

    public DepFile() {
    }

    public DepFile(byte[] file) {
        this.file = file;
    }
}
