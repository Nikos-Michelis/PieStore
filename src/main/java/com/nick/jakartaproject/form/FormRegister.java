package com.nick.jakartaproject.form;


import com.nick.jakartaproject.customvalidators.EmailNotExistsConstraint;
import com.nick.jakartaproject.customvalidators.UserNameNotExistsConstraint;
import jakarta.validation.constraints.*;

import java.io.Serializable;

public class FormRegister implements Serializable {
    @NotNull (message = "The full name must not be null")
    @NotEmpty (message = "The full name must not be empty")
    private String fullName;
    @NotNull(message = "The e-mail must not be null")
    @NotEmpty (message = "The e-mail must not be empty")
    @Email (message = "The e-mail is not valid")
    @EmailNotExistsConstraint(message = "User email already exists select new!")
    private String email;
    @Pattern(regexp = "^[26][0-9]{9}$|^$", message = "Invalid phone number")
    private String tel;
    @Pattern(regexp = "^[0-9a-zA-Z]{5,20}$|^$", message = "Invalid username (5-20 Latin characters and/or numbers)")
    @UserNameNotExistsConstraint(message = "Username already exists select new one!")
    private String userName;
    @Pattern(regexp = "^[0-9a-zA-Z]{5,20}$|^$", message = "Invalid password (5-20 Latin characters and/or numbers)")
    private String password;
    @Pattern(regexp = "^[0-9a-zA-Z]{5,20}$|^$", message = "Invalid password (5-20 Latin characters and/or numbers)")
    private String repeatPassword;
    @AssertTrue(message = "the passwords you provided are not the same")
    private Boolean passwordMatch;
    public FormRegister(){}

    public FormRegister(String fullName, String email, String tel, String userName, String password, String repeatPassword) {
        this.fullName = fullName;
        this.email = email;
        this.tel = tel;
        this.userName = userName;
        this.password = password;
        this.repeatPassword = repeatPassword;
        // check if two passwords matching and if is true save the boolean result to passwordMatch variable
        this.passwordMatch = repeatPassword.equals(password);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NotNull String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", repeatPassword='" + repeatPassword + '\'' +
                '}';
    }
}

