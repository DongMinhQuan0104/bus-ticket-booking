package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TripRepo{
    void save(Trip trip);

    Optional<Trip> findById(UUID id);

    void delete(Trip trip);

    Page<Trip> findAll(Pageable pageable);

    Page<Trip> findByRouteNameContainingIgnoreCase(String name, Pageable pageable);
}
