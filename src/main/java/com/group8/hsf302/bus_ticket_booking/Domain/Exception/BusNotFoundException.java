package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class BusNotFoundException extends RuntimeException {
    public BusNotFoundException() {
        super("Bus Not Founded");
    }
}
