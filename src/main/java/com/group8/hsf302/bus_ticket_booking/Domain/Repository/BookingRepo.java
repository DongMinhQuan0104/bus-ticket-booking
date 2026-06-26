package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepo {
    Optional<Booking> findById(UUID id);
}
