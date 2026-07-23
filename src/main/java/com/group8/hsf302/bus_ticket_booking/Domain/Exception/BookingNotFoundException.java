package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException() {
        super("Booking not found");
    }
}
