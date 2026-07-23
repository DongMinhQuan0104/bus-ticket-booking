package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Bus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface BusRepo {
    Optional<Bus> findByLicensePlate(String licensePlate);

    void save(Bus bus);

    Page<Bus> findAll(Pageable pageable);

    Page<Bus> findByBusNameContaining(String name, Pageable pageable);

    Optional<Bus> findById(UUID id);

    void delete(Bus bus);
}
