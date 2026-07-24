package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepo {
    Booking save(Booking booking);

    Optional<Booking> findById(UUID id);

    Optional<Booking> findByBookingCode(String bookingCode);

    List<Booking> search(String keyword);

    List<Booking> findExpiredPendingBookings(LocalDateTime now);
}