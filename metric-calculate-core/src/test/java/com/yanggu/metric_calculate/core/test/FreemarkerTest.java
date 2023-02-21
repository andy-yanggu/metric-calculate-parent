package com.yanggu.metric_calculate.core.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.value.Key;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.codehaus.janino.ScriptEvaluator;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试使用freemarker动态生成java代码
 */
public class FreemarkerTest {

    /**
     * 动态生成集合类代码
     */
    @Test
    public void testCreateCollectionUnit() throws Exception {
        Version version = new Version("2.3.28");
        Configuration configuration = new Configuration(version);
        configuration.setDefaultEncoding("utf-8");
        //模板文件路径
        String fileName = "collection.ftl";
        configuration.setDirectoryForTemplateLoading(new File(getTemplatePath(fileName)));
        StringWriter stringWriter = new StringWriter();
        Template template = configuration.getTemplate(fileName);

        Map<String, Object> param = new HashMap<>();
        param.put("fullName", "com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit");
        param.put("useParam", true);

        template.process(param, stringWriter);
        System.out.println("生成的模板代码");
        System.out.println(stringWriter);

        ScriptEvaluator evaluator = new ScriptEvaluator();
        String expression = stringWriter.toString();
        String[] parameterNames = {"param", "initValue"};
        Class<?>[] parameterTypes = {Map.class, Object.class};
        evaluator.setParameters(parameterNames, parameterTypes);
        evaluator.setReturnType(Object.class);
        evaluator.setParentClassLoader(ClassLoader.getSystemClassLoader());
        evaluator.cook(expression);

        Map<String, Object> param2 = new HashMap<>();
        param2.put("limit", 10);
        Object evaluate = evaluator.evaluate(param2, new Key<>(10));
        System.out.println(evaluate);

    }

    @Test
    public void testCreateNumberUnit() throws Exception {
        Version version = new Version("2.3.28");
        Configuration configuration = new Configuration(version);
        configuration.setDefaultEncoding("utf-8");
        //模板文件路径
        String fileName = "number.ftl";
        configuration.setDirectoryForTemplateLoading(new File(getTemplatePath(fileName)));
        StringWriter stringWriter = new StringWriter();
        Template template = configuration.getTemplate(fileName);

        Map<String, Object> param = new HashMap<>();
        param.put("fullName", "com.yanggu.metric_calculate.core.unit.numeric.CountUnit");
        param.put("useParam", false);


        template.process(param, stringWriter);
        System.out.println("生成的模板代码");
        System.out.println(stringWriter);

        ScriptEvaluator evaluator = new ScriptEvaluator();
        String expression = stringWriter.toString();
        String[] parameterNames = {"param", "initValue"};
        Class<?>[] parameterTypes = {Map.class, CubeNumber.class};
        evaluator.setParameters(parameterNames, parameterTypes);
        evaluator.setReturnType(Object.class);
        evaluator.setParentClassLoader(ClassLoader.getSystemClassLoader());
        evaluator.cook(expression);

        Map<String, Object> param2 = new HashMap<>();
        Object evaluate = evaluator.evaluate(param2, CubeLong.of(1L));
        System.out.println(evaluate);
    }

    private String getTemplatePath(String fileName) {
        //返回读取指定资源的输入流
        InputStream is = this.getClass().getResourceAsStream("/merged_unit_template/" + fileName);
        String path = System.getProperty("java.io.tmpdir");
        String dirPath = path + File.separator + IdUtil.fastSimpleUUID() + "/templates";
        //System.out.println(dirPath);
        File dir = new File(dirPath);
        //create folder
        if (!dir.mkdirs()) {
            return dirPath;
        }
        String filePath = dirPath + File.separator + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            return dirPath;
        }
        //文件不存在，则创建流输入默认数据到新文件
        FileUtil.writeFromStream(is, file, true);
        return dirPath;
    }

}
