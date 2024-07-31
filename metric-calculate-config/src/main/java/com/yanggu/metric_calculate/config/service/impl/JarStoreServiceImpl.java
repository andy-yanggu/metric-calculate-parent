package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.JarStoreMapper;
import com.yanggu.metric_calculate.config.domain.entity.JarStoreEntity;
import com.yanggu.metric_calculate.config.service.JarStoreService;
import org.springframework.stereotype.Service;

/**
 * jar包存储 服务层实现。
 *
 * @author MondayLi
 * @since 2023-08-29
 */
@Service
public class JarStoreServiceImpl extends ServiceImpl<JarStoreMapper, JarStoreEntity> implements JarStoreService {

}
