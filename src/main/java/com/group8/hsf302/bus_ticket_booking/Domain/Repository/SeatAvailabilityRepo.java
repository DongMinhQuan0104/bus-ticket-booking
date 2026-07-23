package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;

import java.util.List;
import java.util.UUID;

public interface SeatAvailabilityRepo {
    long countBookedSeats(UUID tripId);

    List<String> findOccupiedSeatCodes(UUID tripId);

    List<String> findTakenSeatCodes(UUID tripId, List<String> seatCodes);

    List<SeatAvailability> findByBookingId(UUID bookingId);

    SeatAvailability save(SeatAvailability seatAvailability);

    void delete(SeatAvailability seatAvailability);
}
