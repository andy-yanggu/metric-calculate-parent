package com.yanggu.metric_calculate.config.config;

import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.util.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.yanggu.metric_calculate.config.enums.ResultCode.NO_LOGIN;
import static com.yanggu.metric_calculate.config.util.Constant.USER_ID;

/**
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader(USER_ID);
        if (StrUtil.isBlank(userId)) {
            throw new BusinessException(NO_LOGIN);
        }
        ThreadLocalUtil.setUserId(Integer.parseInt(userId));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.removeUserId();
    }

}