package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class BookingDetailNotFoundException extends RuntimeException {
    public BookingDetailNotFoundException() {
        super("Booking Detail Not Found");
    }
}
