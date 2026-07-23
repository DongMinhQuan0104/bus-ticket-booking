package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Review;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepo {
    Review save(Review review);
    boolean existsByBookingId(UUID bookingId);
    Optional<Review> findByBookingId(UUID bookingId);
}
