package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TripJpaRepo extends JpaRepository<Trip, UUID> {

    List<Trip> findByStatusAndDestinationFromIgnoreCaseAndDestinationToIgnoreCaseAndDepartureTimeBetweenOrderByDepartureTimeAsc(
            Status status, String destinationFrom, String destinationTo,
            LocalDateTime start, LocalDateTime end);
}
