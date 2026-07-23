package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Station;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StationJpaRepo extends JpaRepository<Station, UUID> {
    Optional<Station> findByName(String name);

    Page<Station> findByNameContaining(String name, Pageable pageable);
}
