package com.nick.jakartaproject.customvalidators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Constraint(validatedBy = UserNameNotExistsValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNameNotExistsConstraint {
    String message() default "Email already exists!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}