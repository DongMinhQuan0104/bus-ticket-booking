package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginForm {

    @NotBlank(message = "email can not blank")
    @Email(message = "email not in the right format")
    private String email;

    @NotBlank(message = "password can not blank")
    private String password;

    public LoginForm() {
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
}
