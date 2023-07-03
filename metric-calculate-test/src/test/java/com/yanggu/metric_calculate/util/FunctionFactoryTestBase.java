package com.yanggu.metric_calculate.util;

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FunctionFactoryTestBase {

    public static final String TEST_JAR_PATH;

    static {
        try {
            TEST_JAR_PATH = testJarPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用maven命令生产metric-calculate-test-1.0.0-SNAPSHOT.jar包
     *
     * @return jar路径
     * @throws Exception
     */
    public static String testJarPath() throws Exception {
        String testModuleName = "metric-calculate-test";
        String version = "1.0.0-SNAPSHOT";
        String separator = File.separator;

        String canonicalPath = new File("").getCanonicalPath();
        //canonicalPath = canonicalPath.substring(0, canonicalPath.lastIndexOf(separator));
        String directoryPath = canonicalPath + separator + testModuleName + separator + "target";
        File directory = new File(directoryPath);

        String pathname = directoryPath + separator + testModuleName + "-" + version + ".jar ";
        if (directory.exists()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                String name = file.getName();
                if (file.isFile() && name.startsWith(testModuleName) && name.endsWith(".jar")) {
                    pathname = file.getAbsolutePath();
                    break;
                }
            }
        }
        System.out.println(pathname);
        File file = new File(pathname);
        if (file.exists()) {
            long lastModified = file.lastModified();
            //如果新生成的jar包在3min内, 就直接返回
            if (Math.abs(System.currentTimeMillis() - lastModified) <= TimeUnit.MINUTES.toMillis(3L)) {
                return pathname;
            }
        }

        //使用maven命令进行打包
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(canonicalPath + separator + "pom.xml"));
        System.out.println(canonicalPath + separator + "pom.xml");
        request.setGoals(Collections.singletonList("package"));
        request.setProjects(Collections.singletonList(testModuleName));
        request.setAlsoMake(true);
        request.setThreads("2.0C");
        request.setMavenOpts("-Dmaven.test.skip=true");
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
        InvocationResult execute = invoker.execute(request);
        if (execute.getExitCode() != 0) {
            throw new RuntimeException(testModuleName + "打包失败, 单元测试执行失败");
        }
        return pathname;
    }

    public static void main(String[] args) {
        System.out.println("testJarPath = " + TEST_JAR_PATH);
    }

}
