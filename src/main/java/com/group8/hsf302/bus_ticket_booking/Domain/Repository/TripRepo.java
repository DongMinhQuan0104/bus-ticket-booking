package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripRepo {
    List<Trip> findByDriverName(String driverName);
    Optional<Trip> findById(UUID id);
    Trip save(Trip trip);
}
