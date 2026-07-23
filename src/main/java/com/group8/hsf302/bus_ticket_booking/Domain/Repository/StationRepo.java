package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StationRepo{
    Optional<Station> findByName(String name);

    void save(Station station);

    Page<Station> findAll(Pageable pageable);

    Page<Station> findByNameContaining(String name, Pageable pageable);

    Optional<Station> findById(UUID id);

    void delete(Station station);
}
