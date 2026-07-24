package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
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

    // ===== Customer (Viet) - E1 =====
    // "Con dat duoc" = chuyen o trang thai SCHEDULED (thong nhat 1 truong TripStatus).
    @Override
    public List<Trip> searchAvailable(String destinationFrom, String destinationTo,
                                      LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return tripJpaRepo
                .findByStatusAndDestinationFromIgnoreCaseAndDestinationToIgnoreCaseAndDepartureTimeBetweenOrderByDepartureTimeAsc(
                        TripStatus.SCHEDULED, destinationFrom, destinationTo, startOfDay, endOfDay);
    }

    @Override
    public Optional<Trip> findById(UUID id) {
        return tripJpaRepo.findById(id);
    }

    // ===== Driver (An) =====
    @Override
    public List<Trip> findByDriverName(String driverName) {
        return tripJpaRepo.findByDriverName(driverName);
    }

    @Override
    public Trip save(Trip trip) {
        return tripJpaRepo.save(trip);
    }

    // ===== Admin (B4) =====
    @Override
    public Page<Trip> findAll(Pageable pageable) {
        return tripJpaRepo.findAll(pageable);
    }

    @Override
    public Page<Trip> findByRouteNameContainingIgnoreCase(String name, Pageable pageable) {
        return tripJpaRepo.findByRouteNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public void delete(Trip trip) {
        tripJpaRepo.delete(trip);
    }

    // ===== Goi y thanh pho (E1) =====
    @Override
    public List<String> findDistinctDepartureCities() {
        return tripJpaRepo.findDistinctDepartureCities();
    }

    @Override
    public List<String> findDistinctArrivalCities() {
        return tripJpaRepo.findDistinctArrivalCities();
    }
}
