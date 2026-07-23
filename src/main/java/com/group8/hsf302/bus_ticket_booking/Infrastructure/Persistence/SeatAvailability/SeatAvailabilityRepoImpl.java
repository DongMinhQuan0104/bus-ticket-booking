package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SeatAvailability;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.SeatAvailabilityRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class SeatAvailabilityRepoImpl implements SeatAvailabilityRepo {

    private final SeatAvailabilityJpaRepo seatAvailabilityJpaRepo;

    public SeatAvailabilityRepoImpl(SeatAvailabilityJpaRepo seatAvailabilityJpaRepo) {
        this.seatAvailabilityJpaRepo = seatAvailabilityJpaRepo;
    }

    @Override
    public long countBookedSeats(UUID tripId) {
        return seatAvailabilityJpaRepo.countByTripIdAndBookingDetailIsNotNull(tripId);
    }

    @Override
    public List<String> findOccupiedSeatCodes(UUID tripId) {
        return seatAvailabilityJpaRepo.findOccupiedSeatCodes(tripId);
    }

    @Override
    public List<String> findTakenSeatCodes(UUID tripId, List<String> seatCodes) {
        return seatAvailabilityJpaRepo.findTakenSeatCodes(tripId, seatCodes);
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
