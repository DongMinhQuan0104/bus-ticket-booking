package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Role;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AdminUpdateAccountForm {

    @NotBlank(message = "full name can not blank")
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank(message = "email can not blank")
    @Email(message = "email not in the right format")
    private String email;

    @NotBlank(message = "password can not blank")
    private String password;

    @Pattern(regexp = "^0\\\\d{9}$", message = "phoneNumber not in the right format")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Status status;

    public AdminUpdateAccountForm() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
