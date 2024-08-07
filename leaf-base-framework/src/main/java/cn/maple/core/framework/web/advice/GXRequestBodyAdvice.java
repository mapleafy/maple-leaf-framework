package cn.maple.core.framework.web.advice;

import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.framework.service.GXRequestBodyAdviceService;
import cn.maple.core.framework.util.GXSpringContextUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

@ConditionalOnBean(value = {GXRequestBodyAdviceService.class})
@RestControllerAdvice
public class GXRequestBodyAdvice extends RequestBodyAdviceAdapter {
    /**
     * Invoked first to determine if this interceptor applies.
     *
     * @param methodParameter the method parameter
     * @param targetType      the target type, not necessarily the same as the method
     *                        parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType   the selected converter type
     * @return whether this interceptor should be invoked or not
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        GXRequestBodyAdviceService requestBodyAdviceService = GXSpringContextUtils.getBean(GXRequestBodyAdviceService.class);
        if (ObjectUtil.isNull(requestBodyAdviceService)) {
            return false;
        }
        return requestBodyAdviceService.supports(methodParameter, targetType, converterType);
    }

    /**
     * Invoked third (and last) after the request body is converted to an Object.
     *
     * @param body          set to the converter Object before the first advice is called
     * @param inputMessage  the request
     * @param parameter     the target method parameter
     * @param targetType    the target type, not necessarily the same as the method
     *                      parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the converter used to deserialize the body
     * @return the same body or a new instance
     */
    @NotNull
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        GXRequestBodyAdviceService requestBodyAdviceService = GXSpringContextUtils.getBean(GXRequestBodyAdviceService.class);
        if (ObjectUtil.isNull(requestBodyAdviceService)) {
            return body;
        }
        return requestBodyAdviceService.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}