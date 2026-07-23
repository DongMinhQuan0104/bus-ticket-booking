package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException() {
        super("Trip Not Found");
    }
}
