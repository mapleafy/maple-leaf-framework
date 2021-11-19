package cn.maple.core.framework.web.support;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.annotation.GXFieldComment;
import cn.maple.core.framework.annotation.GXRequestBody;
import cn.maple.core.framework.code.GXResultCode;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Component
public class GXRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @GXFieldComment(zhDesc = "请求中的参数名字")
    public static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    /**
     * 进行参数验证之前对数据进行修复的方法名字
     */
    private static final String BEFORE_REPAIR_METHOD = "beforeRepair";

    /**
     * 进行参数验证
     */
    private static final String VERIFY_METHOD = "verify";

    /**
     * 进行参数验证之后对数据进行修复的方法名字
     */
    private static final String AFTER_REPAIR_METHOD = "afterRepair";

    @GXFieldComment(zhDesc = "日志对象")
    private static final Logger LOGGER = LoggerFactory.getLogger(GXRequestHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(GXRequestBody.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String body = getRequestBody(webRequest);
        final Class<?> parameterType = parameter.getParameterType();
        if (JSONUtil.isJsonArray(body)) {
            Class<?> actualTypeArgument = (Class<?>) ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];
            List<?> bean = JSONUtil.toList(body, actualTypeArgument);
            bean.forEach(dto -> {
                dealSingleBean(dto, parameter, actualTypeArgument);
            });
            return bean;
        }
        Object bean = Convert.convert(parameterType, JSONUtil.toBean(body, parameterType));
        dealSingleBean(bean, parameter, parameterType);
        return bean;
    }

    /**
     * 处理单个bean数据
     *
     * @param bean          bean对象
     * @param parameter     请求参数对象
     * @param parameterType 请求参数类型
     * @return bean
     */
    private void dealSingleBean(Object bean, MethodParameter parameter, Class<?> parameterType) {
        GXRequestBody requestBody = parameter.getParameterAnnotation(GXRequestBody.class);
        final String value = Objects.requireNonNull(requestBody).value();
        boolean validateTarget = requestBody.validateTarget();
        // 对请求数据进行验证之前的修复处理
        callUserDefinedMethod(parameterType, bean, BEFORE_REPAIR_METHOD);

        // 调用目标bean对象的验证方法对数据进行验证(自定义验证)
        callUserDefinedMethod(parameterType, bean, VERIFY_METHOD);

        // 数据验证
        Class<?>[] groups = requestBody.groups();
        if (validateTarget) {
            if (parameter.hasParameterAnnotation(Validated.class)) {
                groups = Objects.requireNonNull(parameter.getParameterAnnotation(Validated.class)).value();
            }
            GXValidatorUtils.validateEntity(bean, value, groups);
        }

        // 调用目标bean对象的修复方法对数据进行最后的修复
        callUserDefinedMethod(parameterType, bean, AFTER_REPAIR_METHOD);
        return;
    }

    /**
     * 调用补充的修复数据方法对当前对象进行额外数据修复处理
     * 调用自定义的方法对数据进行额外的处理
     *
     * @param parameterType 参数的类型
     * @param bean          对象的名字
     * @param methodName    方法名字
     */
    private void callUserDefinedMethod(Class<?> parameterType, Object bean, String methodName) {
        Method method = ReflectUtil.getMethodByName(parameterType, methodName);
        if (Objects.nonNull(method)) {
            ReflectUtil.invoke(bean, method);
        }
    }

    /**
     * 获取请求的数据
     *
     * @param webRequest NativeWebRequest对象
     * @return String
     */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        assert servletRequest != null;
        String jsonBody = (String) servletRequest.getAttribute(JSON_REQUEST_BODY);
        if (null == jsonBody) {
            try {
                jsonBody = IoUtil.read(servletRequest.getInputStream(), StandardCharsets.UTF_8);
                servletRequest.setAttribute(JSON_REQUEST_BODY, jsonBody);
            } catch (IOException e) {
                throw new GXBusinessException(e.getMessage(), e);
            }
        }
        if (!JSONUtil.isJson(jsonBody)) {
            throw new GXBusinessException(GXResultCode.REQUEST_JSON_NOT_BODY);
        }
        return jsonBody;
    }
}
