package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException() {
        super("Trip not found or no longer available");
    }
}
