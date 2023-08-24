package com.yanggu.metric_calculate.core.test;


import lombok.SneakyThrows;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class TestJarUtil {

    @SneakyThrows
    public static List<String> testJarPath() {
        String testModuleName = "metric-calculate-test";
        String version = "1.0.0-SNAPSHOT";
        String separator = File.separator;

        String canonicalPath = new File("").getCanonicalPath();
        canonicalPath = canonicalPath.substring(0, canonicalPath.lastIndexOf(separator));
        String directoryPath = canonicalPath + separator + testModuleName + separator + "target";

        String pathname = directoryPath + separator + testModuleName + "-" + version + ".jar";
        File file = new File(pathname);
        if (file.exists()) {
            return Collections.singletonList(pathname);
        } else {
            return Collections.emptyList();
        }
        //System.out.println(pathname);
    }

    public static void main(String[] args) {

    }

}
