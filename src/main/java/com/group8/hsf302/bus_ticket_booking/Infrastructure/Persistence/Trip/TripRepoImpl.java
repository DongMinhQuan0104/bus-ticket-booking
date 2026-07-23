package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TripRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TripRepoImpl implements TripRepo {

    private final TripJpaRepo tripJpaRepo;

    public TripRepoImpl(TripJpaRepo tripJpaRepo) {
        this.tripJpaRepo = tripJpaRepo;
    }

    @Override
    public List<Trip> findByDriverName(String driverName) {
        return tripJpaRepo.findByDriverName(driverName);
    }

    @Override
    public Optional<Trip> findById(UUID id) {
        return tripJpaRepo.findById(id);
    }

    @Override
    public Trip save(Trip trip) {
        return tripJpaRepo.save(trip);
    }
}
