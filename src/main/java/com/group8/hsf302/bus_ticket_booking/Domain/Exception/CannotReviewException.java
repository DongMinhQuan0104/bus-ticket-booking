package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class CannotReviewException extends RuntimeException {
    public CannotReviewException(String message) {
        super(message);
    }
}
