package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class SeatAlreadyBookedException extends RuntimeException {
    public SeatAlreadyBookedException(String seatCodes) {
        super("Seat(s) already booked: " + seatCodes);
    }
}
