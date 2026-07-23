package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class AlreadyCheckedInException extends RuntimeException {
    public AlreadyCheckedInException() {
        super("Passenger ticket has already been checked in.");
    }
}
