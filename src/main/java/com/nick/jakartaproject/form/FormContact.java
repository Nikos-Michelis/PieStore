package com.nick.jakartaproject.form;

import jakarta.validation.constraints.*;

public class FormContact {
    @NotNull (message = "The full name must not be null")
    @NotEmpty (message = "The full name must not be empty")
    private String fullname;

    @NotNull (message = "The e-mail must not be null")
    @NotEmpty (message = "The e-mail must not be empty")
    @Email (message = "Invalid e-mail")
    private String email;

    @Pattern(regexp = "^[26][0-9]{9}$|^$", message = "Invalid phone number")
    private String tel;

    @Pattern(regexp = ".{5,}", message = "The message must be at least 5 characters long")
    @Pattern(regexp = ".{0,100}", message = "The message must be a maximum of 100 characters")
    private String message;

    public FormContact(String fullname, String email, String tel, String message) {
        this.fullname = fullname;
        this.email = email;
        this.tel = tel;
        this.message = message;
    }

    public FormContact() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FormContact{" +
                "fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
