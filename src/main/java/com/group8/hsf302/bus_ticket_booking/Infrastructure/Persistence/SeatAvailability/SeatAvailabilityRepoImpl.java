package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SeatAvailability;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.SeatAvailabilityRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SeatAvailabilityRepoImpl implements SeatAvailabilityRepo {

    private final SeatAvailabilityJpaRepo seatAvailabilityJpaRepo;

    public SeatAvailabilityRepoImpl(SeatAvailabilityJpaRepo seatAvailabilityJpaRepo) {
        this.seatAvailabilityJpaRepo = seatAvailabilityJpaRepo;
    }

    @Override
    public SeatAvailability save(SeatAvailability seatAvailability) {
        return seatAvailabilityJpaRepo.save(seatAvailability);
    }

    @Override
    public List<SeatAvailability> saveAll(List<SeatAvailability> seatAvailabilities) {
        return seatAvailabilityJpaRepo.saveAll(seatAvailabilities);
    }

    @Override
    public Optional<SeatAvailability> findById(UUID id) {
        return seatAvailabilityJpaRepo.findById(id);
    }

    @Override
    public List<SeatAvailability> findByTripId(UUID tripId) {
        return seatAvailabilityJpaRepo.findByTripIdOrderBySeatCodeAsc(tripId);
    }
}