package com.group8.hsf302.bus_ticket_booking.Presentation.Exception;

import com.group8.hsf302.bus_ticket_booking.Domain.Exception.EmailAlreadyExistsException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.InvalidCredentialsException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.PasswordConfirmNotMatchException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExist(EmailAlreadyExistsException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "index";
    }

    @ExceptionHandler(PasswordConfirmNotMatchException.class)
    public String handlePasswordNotMatch(PasswordConfirmNotMatchException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "index";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(InvalidCredentialsException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "index";
    }
}
