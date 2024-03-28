package com.nick.jakartaproject.form;

import com.nick.jakartaproject.models.dao.UserDAO;
import jakarta.validation.constraints.*;

import java.io.Serializable;

public class FormLogin implements Serializable {
    private String userName;
    private String password;
    @AssertTrue(message = "Invalid username or password!")
    private Boolean validUser;
    public FormLogin(){}

    public FormLogin(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.validUser = UserDAO.userLogin(userName, password) != null;
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

    public Boolean getValidUser() {
        return validUser;
    }

    public void setValidUser(Boolean validUser) {
        this.validUser = validUser;
    }

    @Override
    public String toString() {
        return "FormLogin{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", validUser=" + validUser +
                '}';
    }
}

