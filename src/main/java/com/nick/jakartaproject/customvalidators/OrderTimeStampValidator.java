package com.nick.jakartaproject.customvalidators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class OrderTimeStampValidator implements ConstraintValidator<OrderTimeStampValidatorConstraint, LocalDateTime> {
    @Override
    public void initialize(OrderTimeStampValidatorConstraint constraintAnnotation) {
    }
    @Override
    public boolean isValid(LocalDateTime timestamps, ConstraintValidatorContext constraintValidatorContext) {

        /*if (timestamps.getHour() < 8 || timestamps.getHour() >= 23){
            return false;
            // else if the time is over 22 for example 22:01 the return again false
        }else if (timestamps.getMinute() > 0 || timestamps.getSecond() > 0 || timestamps.getNano() > 0){
            return false;
        }*/
        return true;
    }
}

