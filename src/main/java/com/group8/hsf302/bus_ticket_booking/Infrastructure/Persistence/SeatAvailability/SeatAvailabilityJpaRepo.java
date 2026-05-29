package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SeatAvailability;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeatAvailabilityJpaRepo extends JpaRepository<SeatAvailability, UUID> {
}
