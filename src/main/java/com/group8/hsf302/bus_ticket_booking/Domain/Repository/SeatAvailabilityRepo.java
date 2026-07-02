package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatAvailabilityRepo {

    SeatAvailability save(SeatAvailability seatAvailability);

    List<SeatAvailability> saveAll(List<SeatAvailability> seatAvailabilities);

    Optional<SeatAvailability> findById(UUID id);

    List<SeatAvailability> findByTripId(UUID tripId);
}