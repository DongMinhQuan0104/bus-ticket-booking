package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripRepo {
    Optional<Trip> findById(UUID id);

    List<Trip> searchAvailableTrips(
            String from,
            String to,
            LocalDateTime start,
            LocalDateTime end
    );
}