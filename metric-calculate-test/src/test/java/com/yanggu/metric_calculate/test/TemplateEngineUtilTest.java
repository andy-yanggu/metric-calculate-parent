package com.yanggu.metric_calculate.test;

import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateConfig;
import org.dromara.hutool.extra.template.TemplateUtil;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p><a href="https://hutool.cn/docs/#/extra/%E6%A8%A1%E6%9D%BF%E5%BC%95%E6%93%8E/%E6%A8%A1%E6%9D%BF%E5%BC%95%E6%93%8E%E5%B0%81%E8%A3%85-TemplateUtil">hutool模板引擎封装-TemplateUtil地址</a></p>
 */
class TemplateEngineUtilTest {

    /**
     * 从字符串模板渲染内容
     */
    @Test
    void test1() {
        //自动根据用户引入的模板引擎库的jar来自动选择使用的引擎
        //TemplateConfig为模板引擎的选项，可选内容有字符编码、模板路径、模板加载方式等，默认通过模板字符串渲染
        TemplateEngine engine = TemplateUtil.getEngine().init(new TemplateConfig());

        //假设我们引入的是Freemarker引擎，则：
        Template template = engine.getTemplate("Hello ${name}");
        //Dict本质上为Map，此处可用Map
        String result = template.render(Dict.of().set("name", "Hutool"));
        //输出：Hello Hutool
        assertEquals("Hello Hutool", result);
    }

    /**
     * 从字符串模板渲染内容
     */
    @Test
    void test2() {
        TemplateEngine engine = TemplateUtil.getEngine().init(new TemplateConfig("test_template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("test.ftl");
        String result = template.render(Dict.of().set("name", "Hutool"));
        assertEquals("Hello Hutool", result);
    }

}
