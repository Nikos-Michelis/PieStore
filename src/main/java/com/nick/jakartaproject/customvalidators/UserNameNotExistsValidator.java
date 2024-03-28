package com.nick.jakartaproject.customvalidators;

import com.nick.jakartaproject.models.dao.UserDAO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserNameNotExistsValidator implements ConstraintValidator<UserNameNotExistsConstraint, String> {
    @Override
    public void initialize(UserNameNotExistsConstraint constraintAnnotation) {
    }
    @Override
    public boolean isValid(String userName, ConstraintValidatorContext constraintValidatorContext) {
        return UserDAO.getUserByUserName(userName) == null;
    }
}