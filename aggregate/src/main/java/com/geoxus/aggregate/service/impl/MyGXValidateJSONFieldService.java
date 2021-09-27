package com.geoxus.aggregate.service.impl;

import com.geoxus.common.validator.GXValidateJSONFieldService;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;

@Service
public class MyGXValidateJSONFieldService implements GXValidateJSONFieldService {
    @Override
    public boolean validateJsonFieldData(Object o, String tableName, String parentFieldName, String fieldName, ConstraintValidatorContext context) throws UnsupportedOperationException {
        System.out.println(fieldName + " --- Hello");
        return true;
    }
}
