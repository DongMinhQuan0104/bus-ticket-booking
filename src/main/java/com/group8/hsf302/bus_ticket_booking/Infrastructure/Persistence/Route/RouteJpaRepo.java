package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Route;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteJpaRepo extends JpaRepository<Route, UUID> {
    Optional<Route> findByName(String name);

    Page<Route> findByNameContaining(String name, Pageable pageable);
}
