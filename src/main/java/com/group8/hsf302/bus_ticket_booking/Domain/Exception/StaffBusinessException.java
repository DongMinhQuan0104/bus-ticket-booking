package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class StaffBusinessException extends RuntimeException {

    public StaffBusinessException(String message) {
        super(message);
    }

    public StaffBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}