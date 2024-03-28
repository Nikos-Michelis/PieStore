package com.nick.jakartaproject.form;

import jakarta.validation.constraints.*;
import java.io.Serializable;

public class FormResetPassword implements Serializable {
    @Pattern(regexp = "^[0-9a-zA-Z]{5,20}$|^$", message = "Invalid password (5-20 Latin characters and/or numbers)")
    private String password;
    @Pattern(regexp = "^[0-9a-zA-Z]{5,20}$|^$", message = "Invalid password (5-20 Latin characters and/or numbers)")
    private String repeatPassword;
    @AssertTrue(message = "the passwords you provided are not the same")
    private Boolean passwordMatch;
    public FormResetPassword(){}

    public FormResetPassword(String password, String repeatPassword) {
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.passwordMatch = repeatPassword.equals(password);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public Boolean getPasswordMatch() {
        return passwordMatch;
    }

    public void setPasswordMatch(Boolean passwordMatch) {
        this.passwordMatch = passwordMatch;
    }

    @Override
    public String toString() {
        return "FormRegister{" +
                ", password='" + password + '\'' +
                ", repeatPassword='" + repeatPassword + '\'' +
                '}';
    }
}

