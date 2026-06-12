package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class LoginFailException extends RuntimeException {
    public LoginFailException(String message) {
        super(message);
    }
}
