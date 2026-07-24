package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TripJpaRepo extends JpaRepository<Trip, UUID> {

    Page<Trip> findByRouteNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    @Query("""
            select t
            from Trip t
            left join fetch t.route
            left join fetch t.bus
            where lower(t.destinationFrom) = lower(:from)
              and lower(t.destinationTo) = lower(:to)
              and t.departureTime >= :start
              and t.departureTime <= :end
              and t.status = :status
            order by t.departureTime asc
            """)
    List<Trip> searchAvailableTrips(
            @Param("from") String from,
            @Param("to") String to,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") Status status
    );
}