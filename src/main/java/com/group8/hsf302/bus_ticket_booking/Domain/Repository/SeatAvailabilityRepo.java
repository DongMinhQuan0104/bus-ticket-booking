package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SeatAvailabilityRepo {

    List<SeatAvailability> findOverlappingSegments(
            UUID tripId,
            Integer pickupOrder,
            Integer dropoffOrder
    );

    List<SeatAvailability> lockOverlappingSegments(
            UUID tripId,
            List<String> seatCodes,
            Integer pickupOrder,
            Integer dropoffOrder
    );

    List<SeatAvailability> findByBookingDetailIds(
            List<UUID> bookingDetailIds
    );

    List<SeatAvailability> findExpiredHeldSeats(
            LocalDateTime now
    );

    List<SeatAvailability> saveAll(
            List<SeatAvailability> seats
    );
}