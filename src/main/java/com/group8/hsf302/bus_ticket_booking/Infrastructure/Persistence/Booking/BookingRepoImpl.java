package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Booking;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookingRepoImpl implements BookingRepo {

    private final BookingJpaRepo bookingJpaRepo;

    public BookingRepoImpl(BookingJpaRepo bookingJpaRepo) {
        this.bookingJpaRepo = bookingJpaRepo;
    }

    @Override
    public Booking save(Booking booking) {
        return bookingJpaRepo.save(booking);
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return bookingJpaRepo.findById(id);
    }

    @Override
    public List<Booking> findByAccountId(UUID accountId) {
        return bookingJpaRepo.findByAccountIdOrderByDateBookedDesc(accountId);
    }

    @Override
    public void delete(Booking booking) {
        bookingJpaRepo.delete(booking);
    }
}
