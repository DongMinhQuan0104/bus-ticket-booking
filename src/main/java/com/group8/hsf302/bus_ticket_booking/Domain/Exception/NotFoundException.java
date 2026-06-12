package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
