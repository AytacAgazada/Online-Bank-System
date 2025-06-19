package com.example.onlinebanksystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class OneOfFieldsNotBlankValidator implements ConstraintValidator<OneOfFieldsNotBlank, Object> {

    private String[] fieldNames;
    private String message;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        boolean atLeastOneNotBlank = false;

        for (String fieldName : fieldNames) {
            if (!beanWrapper.isReadableProperty(fieldName)) {
                // Burada loglama əlavə et, ya da exception at
                continue;
            }
            Object fieldValue = beanWrapper.getPropertyValue(fieldName);
            if (fieldValue instanceof String) {
                if (!((String) fieldValue).trim().isEmpty()) {
                    atLeastOneNotBlank = true;
                    break;
                }
            } else if (fieldValue != null) {
                atLeastOneNotBlank = true;
                break;
            }
        }

        if (!atLeastOneNotBlank) {
            context.disableDefaultConstraintViolation();
            for (String fieldName : fieldNames) {
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(fieldName)
                        .addConstraintViolation();
            }
        }

        return atLeastOneNotBlank;
    }

}