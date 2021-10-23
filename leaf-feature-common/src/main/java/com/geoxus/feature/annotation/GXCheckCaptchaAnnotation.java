package com.geoxus.feature.annotation;

import com.geoxus.core.framework.constant.GXCommonConstant;

import java.lang.annotation.*;

/**
 * 验证请求中的验证码注解
 * 方法只要加上该注解就会在该方法执行之前验证
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXCheckCaptchaAnnotation {
    boolean value() default true;

    int verifyType() default GXCommonConstant.CAPTCHA_VERIFY;
}
