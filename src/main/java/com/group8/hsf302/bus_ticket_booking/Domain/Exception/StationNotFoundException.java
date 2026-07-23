package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException() {
        super("Station Not Found");
    }
}
