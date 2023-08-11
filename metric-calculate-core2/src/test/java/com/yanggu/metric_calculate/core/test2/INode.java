package com.yanggu.metric_calculate.core.test2;


public interface INode {

    void execute(PipelineContext pipelineContext);

    String name();

    INode nextNode();

    String kind();

}
