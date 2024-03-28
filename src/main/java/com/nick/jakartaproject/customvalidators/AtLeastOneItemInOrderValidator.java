package com.nick.jakartaproject.customvalidators;

import com.nick.jakartaproject.models.domain.OrderItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class AtLeastOneItemInOrderValidator implements ConstraintValidator<AtLeastOneItemInOrderValidatorConstraint, List<OrderItem>> {
    @Override
    public void initialize(AtLeastOneItemInOrderValidatorConstraint constraintAnnotation) {
    }
    @Override
    public boolean isValid(List<OrderItem> orderItemList, ConstraintValidatorContext constraintValidatorContext) {
        int countItems = 0;
        for (var orderItem:orderItemList) {
            countItems += orderItem.getQuantity();
        }

        return countItems > 0;
    }
}
