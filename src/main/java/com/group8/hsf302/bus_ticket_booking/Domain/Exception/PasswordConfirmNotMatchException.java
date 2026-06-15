package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class PasswordConfirmNotMatchException extends RuntimeException {
    public PasswordConfirmNotMatchException() {
        super("Password Confirm Not Match");
    }
}
