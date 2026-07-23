package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface RouteRepo {
    Optional<Route> findByName(String name);

    void save(Route route);

    Page<Route> findAll(Pageable pageable);

    Page<Route> findByNameContaining(String name, Pageable pageable);

    Optional<Route> findById(UUID id);

    void delete(Route route);
}
