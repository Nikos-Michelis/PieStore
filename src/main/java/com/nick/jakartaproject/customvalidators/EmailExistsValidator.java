package com.nick.jakartaproject.customvalidators;

import com.nick.jakartaproject.models.dao.UserDAO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailExistsValidator implements ConstraintValidator<EmailExistsConstraint, String> {
    @Override
    public void initialize(EmailExistsConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return UserDAO.getUserByEmail(email) != null;
    }
}