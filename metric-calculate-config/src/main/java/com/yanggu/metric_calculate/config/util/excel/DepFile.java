package com.yanggu.metric_calculate.config.util.excel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode
@Data
@NoArgsConstructor
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

    public DepFile(byte[] file) {
        this.file = file;
    }

}
