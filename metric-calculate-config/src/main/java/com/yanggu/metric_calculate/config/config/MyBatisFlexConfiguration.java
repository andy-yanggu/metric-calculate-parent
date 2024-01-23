package com.yanggu.metric_calculate.config.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.tenant.TenantFactory;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.yanggu.metric_calculate.config.base.entity.BaseUserEntity;
import com.yanggu.metric_calculate.config.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-flex自定义配置
 */
@Slf4j
@Configuration
public class MyBatisFlexConfiguration implements MyBatisFlexCustomizer {

    @Autowired
    private UserIdInsertListener userIdInsertListener;

    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {
        //开启审计功能
        AuditManager.setAuditEnable(true);

        //设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage ->
                log.info("{}, {}ms", auditMessage.getFullSql(), auditMessage.getElapsedTime()));

        //注册Insert监听器, 父类注册, 子类也相当于注册
        flexGlobalConfig.registerInsertListener(userIdInsertListener, BaseUserEntity.class);
    }

    /**
     * 返回租户id
     *
     * @return
     */
    @Bean
    public TenantFactory tenantFactory() {
        return () -> new Object[]{ThreadLocalUtil.getUserId()};
    }

}