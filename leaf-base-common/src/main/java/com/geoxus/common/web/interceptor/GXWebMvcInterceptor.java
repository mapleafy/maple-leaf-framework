package com.geoxus.common.web.interceptor;

import com.geoxus.common.properties.web.GXWebMvcConfig;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.common.web.support.GXHandlerMethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * WEB MVC配置
 */
@Configuration
@Slf4j
public class GXWebMvcInterceptor implements WebMvcConfigurer {
    @Resource
    private GXWebMvcConfig webMvcConfig;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final List<String> list = webMvcConfig.getUrlPatterns();
        if (Objects.nonNull(GXSpringContextUtil.getBean(GXAuthorizationInterceptor.class))) {
            registry.addInterceptor(Objects.requireNonNull(GXSpringContextUtil.getBean(GXAuthorizationInterceptor.class))).addPathPatterns(list);
        }
        if (Objects.nonNull(GXSpringContextUtil.getBean(GXBaseSSOPermissionInterceptor.class))) {
            registry.addInterceptor(Objects.requireNonNull(GXSpringContextUtil.getBean(GXBaseSSOPermissionInterceptor.class))).addPathPatterns(list);
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        if (Objects.nonNull(GXSpringContextUtil.getBean(GXHandlerMethodArgumentResolver.class))) {
            argumentResolvers.add(GXSpringContextUtil.getBean(GXHandlerMethodArgumentResolver.class));
        }
    }
}
