package com.group8.hsf302.bus_ticket_booking.Application.Service.Authentication;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.LoginForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.RegisterForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;

public interface AuthenticationService {

    public AccountViewModel register(RegisterForm registerForm);

    public AccountViewModel login(LoginForm loginForm);

    public boolean forgotPassword();

    public boolean changePassword();
}
