package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripJpaRepo extends JpaRepository<Trip, UUID> {
    Page<Trip> findByRouteNameContainingIgnoreCase(String name, Pageable pageable);
}
