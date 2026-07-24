package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SeatAvailability;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.SeatStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SeatAvailabilityJpaRepo
        extends JpaRepository<SeatAvailability, UUID> {

    @Query("""
            select s from SeatAvailability s
            where s.trip.id = :tripId
              and s.startStationOrder < :dropoffOrder
              and s.endStationOrder > :pickupOrder
            order by s.seatCode, s.startStationOrder
            """)
    List<SeatAvailability> findOverlappingSegments(
            @Param("tripId") UUID tripId,
            @Param("pickupOrder") Integer pickupOrder,
            @Param("dropoffOrder") Integer dropoffOrder
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(
            @QueryHint(
                    name = "jakarta.persistence.lock.timeout",
                    value = "5000"
            )
    )
    @Query("""
            select s from SeatAvailability s
            where s.trip.id = :tripId
              and s.seatCode in :seatCodes
              and s.startStationOrder < :dropoffOrder
              and s.endStationOrder > :pickupOrder
            order by s.seatCode, s.startStationOrder
            """)
    List<SeatAvailability> lockOverlappingSegments(
            @Param("tripId") UUID tripId,
            @Param("seatCodes") List<String> seatCodes,
            @Param("pickupOrder") Integer pickupOrder,
            @Param("dropoffOrder") Integer dropoffOrder
    );

    @Query("""
            select s from SeatAvailability s
            where s.bookingDetail.id in :detailIds
            """)
    List<SeatAvailability> findByBookingDetailIds(
            @Param("detailIds") List<UUID> detailIds
    );

    List<SeatAvailability>
    findByStatusAndHoldExpiredAtLessThanEqual(
            SeatStatus status,
            LocalDateTime now
    );
}