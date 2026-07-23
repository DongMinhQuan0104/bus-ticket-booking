package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import java.util.UUID;

public interface SeatAvailabilityRepo {
    long countBookedSeats(UUID tripId);
}
