package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TripRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    public void save(Trip trip) {
        tripJpaRepo.save(trip);
    }

    @Override
    public Optional<Trip> findById(UUID id) {
        return tripJpaRepo.findById(id);
    }

    @Override
    public void delete(Trip trip) {
        tripJpaRepo.delete(trip);
    }

    @Override
    public Page<Trip> findAll(Pageable pageable) {
        return tripJpaRepo.findAll(pageable);
    }

    @Override
    public Page<Trip> findByRouteNameContainingIgnoreCase(
            String name,
            Pageable pageable
    ) {
        return tripJpaRepo.findByRouteNameContainingIgnoreCase(
                name,
                pageable
        );
    }

    @Override
    public List<Trip> searchAvailableTrips(
            String from,
            String to,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return tripJpaRepo.searchAvailableTrips(
                from,
                to,
                start,
                end,
                Status.AVAILABLE
        );
    }
}