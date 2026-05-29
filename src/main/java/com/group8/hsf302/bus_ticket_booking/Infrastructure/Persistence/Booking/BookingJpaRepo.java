package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Booking;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingJpaRepo extends JpaRepository<Booking, UUID> {
}
