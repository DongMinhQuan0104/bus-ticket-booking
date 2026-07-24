package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SeatAvailability;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.SeatAvailabilityRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class SeatAvailabilityRepoImpl implements SeatAvailabilityRepo {

    private final SeatAvailabilityJpaRepo seatAvailabilityJpaRepo;

    public SeatAvailabilityRepoImpl(SeatAvailabilityJpaRepo seatAvailabilityJpaRepo) {
        this.seatAvailabilityJpaRepo = seatAvailabilityJpaRepo;
    }

    @Override
    public long countUnavailableSeats(UUID tripId, LocalDateTime now) {
        return seatAvailabilityJpaRepo.countUnavailableSeats(tripId, now);
    }

    @Override
    public List<String> findOccupiedSeatCodes(UUID tripId, LocalDateTime now) {
        return seatAvailabilityJpaRepo.findOccupiedSeatCodes(tripId, now);
    }

    @Override
    public List<String> findTakenSeatCodesByOthers(UUID tripId, List<String> seatCodes,
                                                   LocalDateTime now, UUID accountId) {
        return seatAvailabilityJpaRepo.findTakenSeatCodesByOthers(tripId, seatCodes, now, accountId);
    }

    @Override
    public List<SeatAvailability> findActiveHolds(UUID tripId, UUID accountId, LocalDateTime now) {
        return seatAvailabilityJpaRepo.findActiveHolds(tripId, accountId, now);
    }

    @Override
    @Transactional
    public int deleteExpiredHolds(LocalDateTime now) {
        return seatAvailabilityJpaRepo.deleteExpiredHolds(now);
    }

    @Override
    @Transactional
    public int deleteHoldsOfAccount(UUID tripId, UUID accountId) {
        return seatAvailabilityJpaRepo.deleteHoldsOfAccount(tripId, accountId);
    }

    @Override
    public List<SeatAvailability> findByBookingId(UUID bookingId) {
        return seatAvailabilityJpaRepo.findByBookingDetailBookingId(bookingId);
    }

    @Override
    public SeatAvailability save(SeatAvailability seatAvailability) {
        return seatAvailabilityJpaRepo.save(seatAvailability);
    }

    @Override
    public void delete(SeatAvailability seatAvailability) {
        seatAvailabilityJpaRepo.delete(seatAvailability);
    }
}
