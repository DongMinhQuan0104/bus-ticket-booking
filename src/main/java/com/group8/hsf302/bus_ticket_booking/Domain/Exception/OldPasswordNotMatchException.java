package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class OldPasswordNotMatchException extends RuntimeException {
    public OldPasswordNotMatchException() {
        super("Old password not match");
    }
}
