package com.geoxus.core.framework.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.core.framework.validator.GXValidateModelMapService;
import com.geoxus.common.pojo.GXResultCode;
import com.geoxus.core.framework.entity.GXCoreAttributesEntity;
import com.geoxus.core.framework.service.GXCoreAttributesService;
import com.geoxus.core.framework.service.GXCoreModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class GXValidateModelMapServiceImpl implements GXValidateModelMapService {
    @Resource
    private GXCoreAttributesService coreAttributesService;

    @Resource
    private GXCoreModelService coreModelService;

    @Override
    public boolean validateModelMap(Dict map, String modelName) {
        final Set<String> keySet = map.keySet();
        if (!coreModelService.checkFormKeyMatch(keySet, modelName)) {
            return false;
        }
        for (String key : keySet) {
            final GXCoreAttributesEntity attributesEntity = coreAttributesService.getAttributeByAttributeName(map.getStr(key));
            final boolean matches = Pattern.matches(attributesEntity.getValidationExpression(), map.get(key).toString());
            if (!matches) {
                throw new GXBusinessException(GXResultCode.PARAMETER_VALIDATION_ERROR);
            }
        }
        return true;
    }
}