package com.geoxus.feature.annotation;

import com.geoxus.core.service.GXDBBaseService;
import com.geoxus.common.service.impl.GXCoreModelServiceImpl;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXRecordHistoryAnnotation {
    String originTableName();

    String historyTableName();

    String[] conditionalParameterName() default {};

    Class<? extends GXDBBaseService<?, ?, ?>> service() default GXCoreModelServiceImpl.class;
}
