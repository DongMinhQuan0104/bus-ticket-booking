package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class LicensePlateAlreadyExistsException extends RuntimeException {
    public LicensePlateAlreadyExistsException() {
        super("licensePlate already exists");
    }
}
