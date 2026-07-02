package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TripJpaRepo extends JpaRepository<Trip, UUID> {

    List<Trip> findByDestinationFromIgnoreCaseAndDestinationToIgnoreCaseAndDepartureTimeBetweenAndStatusAndAvailableSeatsGreaterThanEqual(
            String destinationFrom,
            String destinationTo,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay,
            TripStatus status,
            Integer availableSeats
    );
}