package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super("Account Not Founded");
    }
}
