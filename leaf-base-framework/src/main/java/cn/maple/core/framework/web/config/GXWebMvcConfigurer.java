package cn.maple.core.framework.web.config;

import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.core.framework.web.interceptor.GXTraceIdInterceptor;
import cn.maple.core.framework.web.support.GXCustomerHandlerMethodArgumentResolver;
import cn.maple.core.framework.web.support.GXRequestHandlerMethodArgumentResolver;
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
public abstract class GXWebMvcConfigurer implements WebMvcConfigurer {
    @Resource
    private GXTraceIdInterceptor traceIdInterceptor;

    @Resource
    private GXRequestHandlerMethodArgumentResolver requestHandlerMethodArgumentResolver;

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
        registry.addInterceptor(traceIdInterceptor);
        registerCustomer(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(requestHandlerMethodArgumentResolver);
        if (Objects.nonNull(GXSpringContextUtils.getBean(GXCustomerHandlerMethodArgumentResolver.class))) {
            argumentResolvers.add(GXSpringContextUtils.getBean(GXCustomerHandlerMethodArgumentResolver.class));
        }
    }

    protected abstract void registerCustomer(InterceptorRegistry registry);
}
