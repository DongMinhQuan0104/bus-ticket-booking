package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository cho Review (danh gia - E6). save: luu danh gia;
 * existsByBookingId: chan danh gia trung 1 ve; findByBookingId: lay danh gia da co.
 * findAll/count: Admin xem toan bo feedback co phan trang.
 */
public interface ReviewRepo {
    Review save(Review review);
    boolean existsByBookingId(UUID bookingId);
    Optional<Review> findByBookingId(UUID bookingId);

    Page<Review> findAll(Pageable pageable);
    long count();
}
