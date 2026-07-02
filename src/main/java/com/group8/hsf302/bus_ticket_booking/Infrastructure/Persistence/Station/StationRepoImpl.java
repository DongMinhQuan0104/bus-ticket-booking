package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Station;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Station;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.StationRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StationRepoImpl implements StationRepo {

    private final StationJpaRepo stationJpaRepo;

    public StationRepoImpl(StationJpaRepo stationJpaRepo) {
        this.stationJpaRepo = stationJpaRepo;
    }

    @Override
    public void save(Station station) {
        stationJpaRepo.save(station);
    }

    @Override
    public Optional<Station> findById(UUID id) {
        return stationJpaRepo.findById(id);
    }

    @Override
    public List<Station> findAll() {
        return stationJpaRepo.findAll();
    }
}