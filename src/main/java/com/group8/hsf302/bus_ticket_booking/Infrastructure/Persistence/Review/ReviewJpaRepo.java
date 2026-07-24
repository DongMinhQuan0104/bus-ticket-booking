package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Review;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewJpaRepo extends JpaRepository<Review, UUID> {
    boolean existsByBookingId(UUID bookingId);
    Optional<Review> findByBookingId(UUID bookingId);

    // Admin xem feedback: moi nhat truoc.
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
