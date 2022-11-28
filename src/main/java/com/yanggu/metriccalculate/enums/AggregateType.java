package com.yanggu.metriccalculate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AggregateType {
    SUM("sum"),
    AVG("avg"),
    COUNT("count"),
    REPLACED("replaced"),
    OCCUPIED("occupied"),
    MAX("max"),
    MIN("min"),
    VARP("varp"),
    VARS("vars"),
    COV("cov"),
    DISTINCTCOUNT("distinctCount"),
    DISTINCTLIST("distinctList"),
    INCREASECOUNT("increaseCount"),
    DECREASECOUNT("decreaseCount"),
    MAXINCREASECOUNT("maxIncreaseCount"),
    MAXDECREASECOUNT("maxDecreaseCount"),
    MAXCONTINUOUSCOUNT("maxContinuousCount"),

    MAXOBJECT("maxObject"),
    MINOBJECT("minObject"),
    SORTEDLISTOBJECT("sortedListObject");

    private final String name;

}