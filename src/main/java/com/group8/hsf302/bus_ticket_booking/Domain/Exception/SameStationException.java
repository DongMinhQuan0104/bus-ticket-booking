package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class SameStationException extends RuntimeException {
    public SameStationException() {
        super("Departure point and destination point can not be the same");
    }
}
