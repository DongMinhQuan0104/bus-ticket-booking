package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Station;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StationRepo{
    void save(Station station);
    Optional<Station> findById(UUID id);
    List<Station> findAll();
}
