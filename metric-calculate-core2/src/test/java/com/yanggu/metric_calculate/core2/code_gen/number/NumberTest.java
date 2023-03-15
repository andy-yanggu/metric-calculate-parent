package com.yanggu.metric_calculate.core2.code_gen.number;


import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NumberTest {

    @Test
    public void test1() {
        TemplateConfig templateConfig = new TemplateConfig("merged_unit_template", TemplateConfig.ResourceMode.CLASSPATH);
        TemplateEngine engine = TemplateUtil.createEngine(templateConfig);
        Template template = engine.getTemplate("number.ftl");

        Map<String, Object> param = new HashMap<>();

        //放入全类名
        param.put("class_name", "Double");

        //生成模板代码
        String templateCode = template.render(param);
        System.out.println(templateCode);
    }

}
