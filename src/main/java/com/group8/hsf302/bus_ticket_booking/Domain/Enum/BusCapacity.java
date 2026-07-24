package com.group8.hsf302.bus_ticket_booking.Domain.Enum;

public enum BusCapacity {
    SEAT_16(16),
    SEAT_32(32);

    private final int seats;

    BusCapacity(int seats) {
        this.seats = seats;
    }

    public int getSeats() {
        return seats;
    }
}
