package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SeatAvailability;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeatAvailabilityJpaRepo extends JpaRepository<SeatAvailability, UUID> {

    long countByTripIdAndBookingDetailIsNotNull(UUID tripId);

    @Query("select s.seatCode from SeatAvailability s " +
           "where s.trip.id = :tripId and s.bookingDetail is not null")
    List<String> findOccupiedSeatCodes(@Param("tripId") UUID tripId);
}
