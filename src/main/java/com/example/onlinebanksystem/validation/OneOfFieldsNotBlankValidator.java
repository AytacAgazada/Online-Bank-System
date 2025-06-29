package com.example.onlinebanksystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl; // Bu importu əlavə edin

public class OneOfFieldsNotBlankValidator implements ConstraintValidator<OneOfFieldsNotBlank, Object> {

    private String[] fieldNames;

    @Override
    public void initialize(OneOfFieldsNotBlank constraintAnnotation) {
        this.fieldNames = constraintAnnotation.fieldNames();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // Əgər dəyər (DTO obyekti) null gələrsə, onu invalid hesab edə bilərik
        // Və ya başqa bir @NotNull validasiyası ilə idarə etmək daha yaxşı olar
        if (value == null) {
            return true; // Əgər obyektin özü null-dırsa, bu validasiyanı keçmək olar.
            // Yoxsa, @NotNull kimi digər validasiyalar işləməlidir.
        }

        // BeanWrapperImpl, obyektin sahələrinə daxil olmaq üçün istifadə olunur.
        // Bu hissədə dəyərin 'null' olub olmadığını yoxlayın
        // Məsələn:
        // if (fieldNames == null || fieldNames.length == 0) return true; // Əgər yoxlanacaq sahə yoxdursa

        // Bu hissə NullPointerException-ə səbəb ola bilər
        // if (value == null) return false; // Yaxud true, asılıdır iş məntiqinizdən.

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        boolean atLeastOneNotBlank = false;

        for (String fieldName : fieldNames) {
            Object fieldValue = beanWrapper.getPropertyValue(fieldName);
            if (fieldValue != null && !fieldValue.toString().trim().isEmpty()) {
                atLeastOneNotBlank = true;
                break; // Bir sahə doludursa, digərlərini yoxlamağa ehtiyac yoxdur.
            }
        }

        if (!atLeastOneNotBlank) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}