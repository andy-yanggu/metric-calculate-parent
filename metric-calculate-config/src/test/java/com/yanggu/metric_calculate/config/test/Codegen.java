package com.yanggu.metric_calculate.config.test;

import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.dialect.JdbcTypeMapping;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.entity.BaseEntity;
import com.zaxxer.hikari.HikariDataSource;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

public class Codegen {

    public static void main(String[] args) {
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/metric_calculate_config?characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        GlobalConfig globalConfig = createGlobalConfigUseStyle1();

        //通过 datasource 和 globalConfig 创建代码生成器
        MyGenerator generator = new MyGenerator(dataSource, globalConfig);

        //生成代码
        generator.generate();
    }

    public static GlobalConfig createGlobalConfigUseStyle1() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //产物路径
        globalConfig.setSourceDir("D://test2");
        //设置包名
        globalConfig.setBasePackage("com.yanggu.metric_calculate.config");
        //设置生成的表名
        globalConfig.setGenerateTable("mix_udaf_param_item_base_udaf_param_relation", "mix_udaf_param_item_map_udaf_param_relation");
        //设置生成的mapper的xml路径
        globalConfig.setMapperXmlPath("D://test2/mapper");
        //设置entity的模板路径
        globalConfig.setEntityTemplatePath("entity.tpl");
        //设置entity包名
        globalConfig.setEntityPackageComment("com.yanggu.metric_calculate.config.pojo.entity");
        globalConfig.getStrategyConfig().setTableConfigMap(new HashMap<>());

        //设置生成entity
        globalConfig.enableEntity()
                .setWithLombok(true)
                //设置父类
                .setSuperClass(BaseEntity.class)
                .setOverwriteEnable(true);

        //设置controller
        globalConfig.enableController()
                .setClassSuffix("Controller")
                .setOverwriteEnable(true);

        //设置service
        globalConfig.enableService()
                .setClassSuffix("Service")
                .setSuperClass(IService.class)
                .setOverwriteEnable(true);

        //设置serviceImpl
        globalConfig.enableServiceImpl()
                .setClassSuffix("ServiceImpl")
                .setSuperClass(ServiceImpl.class)
                .setOverwriteEnable(true);

        //设置mapper
        globalConfig.enableMapper()
                .setSuperClass(BaseMapper.class)
                .setClassSuffix("Mapper")
                .setOverwriteEnable(true);

        //设置Mapper.xml
        globalConfig.enableMapperXml()
                .setFileSuffix("Mapper")
                .setOverwriteEnable(true);

        //已经在父类中定义了相关字段
        //设置逻辑删除字段名
        //ColumnConfig isDeletedColumnConfig = new ColumnConfig();
        //isDeletedColumnConfig.setColumnName("is_deleted");
        //isDeletedColumnConfig.setLogicDelete(true);
        //isDeletedColumnConfig.setOnInsertValue("0");
        //globalConfig.setColumnConfig(isDeletedColumnConfig);
        //
        ////设置创建时间
        //ColumnConfig createTimeColumnConfig = new ColumnConfig();
        //createTimeColumnConfig.setColumnName("create_time");
        //createTimeColumnConfig.setOnInsertValue("CURRENT_TIMESTAMP");
        //globalConfig.setColumnConfig(createTimeColumnConfig);
        //
        ////设置更新时间
        //ColumnConfig updateTimeColumnConfig = new ColumnConfig();
        //updateTimeColumnConfig.setColumnName("update_time");
        //updateTimeColumnConfig.setOnInsertValue("CURRENT_TIMESTAMP");
        //updateTimeColumnConfig.setOnUpdateValue("CURRENT_TIMESTAMP");
        //globalConfig.setColumnConfig(updateTimeColumnConfig);

        //禁止生成TableDef
        globalConfig.disableTableDef();

        //设置Date类型
        JdbcTypeMapping.registerMapping(LocalDateTime.class, Date.class);

        return globalConfig;
    }

}