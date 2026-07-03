package com.group8.hsf302.bus_ticket_booking.Presentation.Exception;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.LoginForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.RegisterForm;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.EmailAlreadyExistsException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.InvalidCredentialsException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.PasswordConfirmNotMatchException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExist(EmailAlreadyExistsException e, HttpServletRequest request, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("registerForm", rebuildRegisterForm(request));
        return "register";
    }

    @ExceptionHandler(PasswordConfirmNotMatchException.class)
    public String handlePasswordNotMatch(PasswordConfirmNotMatchException e, HttpServletRequest request, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("registerForm", rebuildRegisterForm(request));
        return "register";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(InvalidCredentialsException e, HttpServletRequest request, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("loginForm", rebuildLoginForm(request));
        return "login";
    }

    private LoginForm rebuildLoginForm(HttpServletRequest request) {
        LoginForm form = new LoginForm();
        form.setEmail(request.getParameter("email"));
        return form;
    }

    private RegisterForm rebuildRegisterForm(HttpServletRequest request) {
        RegisterForm form = new RegisterForm();
        form.setEmail(request.getParameter("email"));
        return form;
    }
}
