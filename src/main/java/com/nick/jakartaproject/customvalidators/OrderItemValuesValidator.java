package com.nick.jakartaproject.customvalidators;

import com.nick.jakartaproject.models.domain.OrderItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class OrderItemValuesValidator implements ConstraintValidator<OrderItemValuesValidatorConstraint, List<OrderItem>> {
    @Override
    public void initialize(OrderItemValuesValidatorConstraint constraintAnnotation) {
    }
    @Override
    public boolean isValid(List<OrderItem> orderItemList, ConstraintValidatorContext constraintValidatorContext) {
        for (var orderItem:orderItemList) {
           if (orderItem.getQuantity() < 0 || orderItem.getQuantity() > 100){
               return false;
           }
        }
        return true;
    }
}
