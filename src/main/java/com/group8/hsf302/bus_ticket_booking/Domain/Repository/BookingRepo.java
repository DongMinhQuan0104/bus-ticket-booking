package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepo {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID id);
    List<Booking> findByAccountId(UUID accountId);
    void delete(Booking booking);
}
