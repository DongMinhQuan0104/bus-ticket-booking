package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Station;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Station;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.StationRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StationRepoImpl implements StationRepo {

    private final StationJpaRepo stationJpaRepo;

    public StationRepoImpl(StationJpaRepo stationJpaRepo) {
        this.stationJpaRepo = stationJpaRepo;
    }

    @Override
    public Optional<Station> findByName(String name) {
        return stationJpaRepo.findByName(name);
    }

    @Override
    public void save(Station station) {
        stationJpaRepo.save(station);
    }

    @Override
    public Page<Station> findAll(Pageable pageable) {
        return stationJpaRepo.findAll(pageable);
    }

    @Override
    public Page<Station> findByNameContaining(String name, Pageable pageable) {
        return stationJpaRepo.findByNameContaining(name,pageable);
    }

    @Override
    public Optional<Station> findById(UUID id) {
        return stationJpaRepo.findById(id);
    }

    @Override
    public void delete(Station station) {
        stationJpaRepo.delete(station);
    }
}
