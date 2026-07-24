package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class CannotCancelBookingException extends RuntimeException {
    public CannotCancelBookingException() {
        super("This booking can no longer be cancelled");
    }
}
