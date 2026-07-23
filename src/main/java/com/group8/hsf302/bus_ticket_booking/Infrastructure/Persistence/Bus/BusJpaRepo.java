package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Bus;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Bus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusJpaRepo extends JpaRepository<Bus, UUID> {
    Optional<Bus> findByLicensePlate(String licensePlate);

    Page<Bus> findByBusNameContaining(String name, Pageable pageable);
}
