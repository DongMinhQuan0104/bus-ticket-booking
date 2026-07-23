package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SeatAvailability;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.SeatStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.SeatAvailabilityRepo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
public class SeatAvailabilityRepoImpl
        implements SeatAvailabilityRepo {

    private final SeatAvailabilityJpaRepo jpaRepo;

    public SeatAvailabilityRepoImpl(
            SeatAvailabilityJpaRepo jpaRepo
    ) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public List<SeatAvailability> findOverlappingSegments(
            UUID tripId,
            Integer pickupOrder,
            Integer dropoffOrder
    ) {
        return jpaRepo.findOverlappingSegments(
                tripId,
                pickupOrder,
                dropoffOrder
        );
    }

    @Override
    public List<SeatAvailability> lockOverlappingSegments(
            UUID tripId,
            List<String> seatCodes,
            Integer pickupOrder,
            Integer dropoffOrder
    ) {
        if (seatCodes == null || seatCodes.isEmpty()) {
            return Collections.emptyList();
        }

        return jpaRepo.lockOverlappingSegments(
                tripId,
                seatCodes,
                pickupOrder,
                dropoffOrder
        );
    }

    @Override
    public List<SeatAvailability> findByBookingDetailIds(
            List<UUID> detailIds
    ) {
        if (detailIds == null || detailIds.isEmpty()) {
            return Collections.emptyList();
        }

        return jpaRepo.findByBookingDetailIds(detailIds);
    }

    @Override
    public List<SeatAvailability> findExpiredHeldSeats(
            LocalDateTime now
    ) {
        return jpaRepo
                .findByStatusAndHoldExpiredAtLessThanEqual(
                        SeatStatus.HELD,
                        now
                );
    }

    @Override
    public List<SeatAvailability> saveAll(
            List<SeatAvailability> seats
    ) {
        return jpaRepo.saveAll(seats);
    }
}