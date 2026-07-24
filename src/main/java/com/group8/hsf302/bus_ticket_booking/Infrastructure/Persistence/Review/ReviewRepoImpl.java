package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Review;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Review;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.ReviewRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ReviewRepoImpl implements ReviewRepo {

    private final ReviewJpaRepo reviewJpaRepo;

    public ReviewRepoImpl(ReviewJpaRepo reviewJpaRepo) {
        this.reviewJpaRepo = reviewJpaRepo;
    }

    @Override
    public Review save(Review review) {
        return reviewJpaRepo.save(review);
    }

    @Override
    public boolean existsByBookingId(UUID bookingId) {
        return reviewJpaRepo.existsByBookingId(bookingId);
    }

    @Override
    public Optional<Review> findByBookingId(UUID bookingId) {
        return reviewJpaRepo.findByBookingId(bookingId);
    }

    @Override
    public Page<Review> findAll(Pageable pageable) {
        return reviewJpaRepo.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public long count() {
        return reviewJpaRepo.count();
    }
}
