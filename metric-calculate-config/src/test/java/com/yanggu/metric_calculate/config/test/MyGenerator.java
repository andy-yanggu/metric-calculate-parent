package com.yanggu.metric_calculate.config.test;

import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.StrategyConfig;
import com.mybatisflex.codegen.dialect.IDialect;
import com.mybatisflex.codegen.entity.Column;
import com.mybatisflex.codegen.entity.Table;
import com.mybatisflex.codegen.generator.GeneratorFactory;
import com.mybatisflex.codegen.generator.IGenerator;
import org.dromara.hutool.core.text.StrUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MyGenerator {

    protected DataSource dataSource;

    protected GlobalConfig globalConfig;

    protected IDialect dialect = IDialect.DEFAULT;

    protected DatabaseMetaData dbMeta = null;

    public MyGenerator(DataSource dataSource, GlobalConfig globalConfig) {
        this.dataSource = dataSource;
        this.globalConfig = globalConfig;
    }

    public MyGenerator(DataSource dataSource, GlobalConfig globalConfig, IDialect dialect) {
        this.dataSource = dataSource;
        this.globalConfig = globalConfig;
        this.dialect = dialect;
    }

    public void generate() {
        try (Connection conn = dataSource.getConnection();) {

            dbMeta = conn.getMetaData();
            List<Table> tables = buildTables(conn);

            for (Table table : tables) {
                List<Column> list = table.getColumns();
                List<Column> add = new ArrayList<>();
                for (Column column : list) {
                    String name = column.getName();
                    boolean match = StrUtil.equalsAny(name, "user_id", "is_deleted", "create_time", "update_time");
                    if (match) {
                        continue;
                    }
                    add.add(column);
                }
                table.setColumns(add);
                Collection<IGenerator> generators = GeneratorFactory.getGenerators();
                for (IGenerator generator : generators) {
                    generator.generate(table, globalConfig);
                }
            }
            System.out.println("Code is generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void buildPrimaryKey(Connection conn, Table table) throws SQLException {
        try (ResultSet rs = dbMeta.getPrimaryKeys(conn.getCatalog(), null, table.getName())) {
            while (rs.next()) {
                String primaryKey = rs.getString("COLUMN_NAME");
                table.addPrimaryKey(primaryKey);
            }
        }
    }

    private List<Table> buildTables(Connection conn) throws SQLException {
        StrategyConfig strategyConfig = globalConfig.getStrategyConfig();
        String schemaName = strategyConfig.getGenerateSchema();
        List<Table> tables = new ArrayList<>();
        try (ResultSet rs = getTablesResultSet(conn, schemaName)) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (!strategyConfig.isSupportGenerate(tableName)) {
                    continue;
                }

                Table table = new Table();
                table.setGlobalConfig(globalConfig);
                table.setTableConfig(strategyConfig.getTableConfig(tableName));

                table.setSchema(schemaName);
                table.setName(tableName);

                String remarks = rs.getString("REMARKS");
                table.setComment(remarks);


                buildPrimaryKey(conn, table);

                dialect.buildTableColumns(schemaName, table, globalConfig, dbMeta, conn);

                tables.add(table);
            }
        }
        return tables;
    }


    protected ResultSet getTablesResultSet(Connection conn, String schema) throws SQLException {
        if (globalConfig.getStrategyConfig().isGenerateForView()) {
            return dialect.getTablesResultSet(dbMeta, conn, schema, new String[]{"TABLE", "VIEW"});
        } else {
            return dialect.getTablesResultSet(dbMeta, conn, schema, new String[]{"TABLE"});
        }
    }

}