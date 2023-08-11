package com.yanggu.metric_calculate.core.test2;

import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
class DecisionFlowTest {

    //@Test
    void test1() {
        PipelineContext pipelineContext = new PipelineContext();
        INode startNode = mock(INode.class);
        when(startNode.name()).thenReturn("start");
        when(startNode.kind()).thenReturn("start");

        INode middleNode = mock(INode.class);
        when(startNode.nextNode()).thenReturn(middleNode);
        when(middleNode.name()).thenReturn("ruleset1");
        when(middleNode.kind()).thenReturn("ruleset");
        doAnswer(invocation -> {
            System.out.println("ruleset1被执行了");
            return null;
        }).when(middleNode).execute(pipelineContext);

        INode endNode = mock(INode.class);
        when(middleNode.nextNode()).thenReturn(endNode);
        when(endNode.name()).thenReturn("end");
        when(endNode.kind()).thenReturn("end");

        INode tempNode;
        while ((tempNode = startNode.nextNode()) != null) {
            System.out.println(tempNode.name() + "正在执行");
            tempNode.execute(pipelineContext);
            if (tempNode.nextNode().kind().equals("end")) {
                break;
            }
        }
    }

}
