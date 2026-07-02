package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException() {
        super("Phone number already exists");
    }
}