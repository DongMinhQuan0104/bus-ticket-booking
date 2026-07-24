package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("Access Denied: You do not have permission to access this resource.");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
