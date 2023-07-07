package com.yanggu.metric_calculate.config;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.dialect.JdbcTypeMapping;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zaxxer.hikari.HikariDataSource;

import java.time.LocalDateTime;
import java.util.Date;

public class Codegen {

    public static void main(String[] args) {
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/metric_calculate_config?characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        //创建配置内容，两种风格都可以。
        GlobalConfig globalConfig = createGlobalConfigUseStyle1();

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        //生成代码
        generator.generate();
    }

    public static GlobalConfig createGlobalConfigUseStyle1() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //设置根包
        globalConfig.setSourceDir("E://test/java");
        globalConfig.setBasePackage("com.yanggu.metric_calculate.config");
        
        globalConfig.setLogicDeleteColumn("is_deleted");

        //设置表前缀和只生成哪些表
        globalConfig.setGenerateSchema("metric_calculate_config");

        //设置生成 entity 并启用 Lombok
        globalConfig.enableEntity().setWithLombok(true).setOverwriteEnable(true);

        globalConfig.enableController().setClassSuffix("Controller");

        globalConfig.enableService().setClassSuffix("Service").setSupperClass(IService.class);

        globalConfig.enableServiceImpl().setClassSuffix("ServiceImpl").setSupperClass(ServiceImpl.class);

        globalConfig.enableMapper().setSupperClass(BaseMapper.class).setClassSuffix("Mapper");

        globalConfig.enableMapperXml();

        globalConfig.disableTableDef();

        JdbcTypeMapping.registerMapping(LocalDateTime.class, Date.class);

        return globalConfig;
    }

}