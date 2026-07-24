package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterForm {

    // Ho ten + SDT: theo thiet ke trang Register (truoc day thieu nen dang ky xong fullName bi null).
    // AccountMapper.toEntity(RegisterForm) map theo ten -> tu dong gan vao Account.
    @NotBlank(message = "full name can not blank")
    private String fullName;

    private String phoneNumber;

    @NotBlank(message = "email can not blank")
    @Email(message = "email not in the right format")
    private String email;

    @NotBlank(message = "password can not blank")
    private String password;

    @NotBlank(message = "Confirmed password can not blank")
    private String confirmPassword;

    public RegisterForm() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
