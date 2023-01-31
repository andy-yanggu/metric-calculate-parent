package com.yanggu.metric_calculate.core.cube;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade {

    private long id;

    private long amt;

    private long time;

    private double exchangeRate;

    private int stat;

    private String city;

    /**
     * Construct.
     */
    public Trade(long id, long amt, long time, String city) {
        this.id = id;
        this.amt = amt;
        this.time = time;
        this.city = city;
        this.exchangeRate = 1.0;
        this.stat = 1;
    }

}
