package com.nick.jakartaproject.form;


import com.nick.jakartaproject.customvalidators.AtLeastOneItemInOrderValidatorConstraint;
import com.nick.jakartaproject.customvalidators.OrderItemValuesValidatorConstraint;
import com.nick.jakartaproject.customvalidators.OrderTimeStampValidatorConstraint;
import com.nick.jakartaproject.models.domain.OrderItem;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.List;

public class FormOrder {
    @NotNull (message = "The full name must not be null")
    @NotEmpty (message = "The full name must not be empty")
    private String fullName;
    @NotNull(message="The address must be filled in")
    @NotEmpty(message="The address must not be empty")
    private String address;

    @NotNull(message="Select Region")
    private Integer areaId;
    @NotNull (message = "The e-mail must not be null")
    @NotEmpty (message = "The e-mail must not be empty")
    @Email (message = "The e-mail is not valid")
    private String email;

    @Pattern(regexp = "^[26][0-9]{9}$|^$", message = "Invalid phone number")
    private String tel;
    private String comments;

    @AtLeastOneItemInOrderValidatorConstraint(message="You must order at least one pie")
    @OrderItemValuesValidatorConstraint(message="You can order from 0-100 of each type of pie")
    private List<OrderItem> orderItems;
    @NotNull(message="A price must be selected for the offer")
    private boolean offer;
    @NotNull(message="You must select payment method")
    private String payment;

    @OrderTimeStampValidatorConstraint(message="The order will not be accepted. Order hours 18:00-22:00")
    private LocalDateTime timestamp;

    public FormOrder() {
    }

    public FormOrder(String fullName, String address, Integer areaId, String email, String tel, String comments,
                     List<OrderItem> orderItems, boolean offer, String payment, LocalDateTime timestamp) {
        this.fullName = fullName;
        this.address = address;
        this.areaId = areaId;
        this.email = email;
        this.tel = tel;
        this.comments = comments;
        this.orderItems = orderItems;
        this.offer = offer;
        this.payment = payment;
        this.timestamp = timestamp;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullname) {
        this.fullName = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public boolean getOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FormOrder{" +
                "\nfullname='" + fullName + '\'' +
                ", \naddress='" + address + '\'' +
                ", \nareaId=" + areaId +
                ", \nemail='" + email + '\'' +
                ", \ntel='" + tel + '\'' +
                ", \ncomments='" + comments + '\'' +
                ", \norderItems=" + orderItems +
                ", \noffer=" + offer +
                ", \npayment='" + payment + '\'' +
                ", \ntimestamp=" + timestamp +
                '}';
    }
}