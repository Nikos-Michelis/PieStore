package com.nick.jakartaproject.form;

import com.nick.jakartaproject.customvalidators.EmailExistsConstraint;
import jakarta.validation.constraints.*;

public class FormResetEmail{
    @NotNull(message = "The e-mail must not be null")
    @NotEmpty (message = "The e-mail must not be empty")
    @Email (message = "The e-mail is not valid")
    @EmailExistsConstraint(message = "The email you provided is not being used by a user!")
    private String email;
    public FormResetEmail(){}

    public FormResetEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "FormRegister{" +
                ", email='" + email + '\'' +
                '}';
    }
}

