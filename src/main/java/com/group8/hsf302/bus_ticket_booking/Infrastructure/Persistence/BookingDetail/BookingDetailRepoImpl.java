package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.BookingDetail;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.BookingDetail;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingDetailRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class BookingDetailRepoImpl implements BookingDetailRepo {

    private final BookingDetailJpaRepo bookingDetailJpaRepo;

    public BookingDetailRepoImpl(BookingDetailJpaRepo bookingDetailJpaRepo) {
        this.bookingDetailJpaRepo = bookingDetailJpaRepo;
    }

    @Override
    public BookingDetail save(BookingDetail bookingDetail) {
        return bookingDetailJpaRepo.save(bookingDetail);
    }

    @Override
    public List<BookingDetail> findByBookingId(UUID bookingId) {
        return bookingDetailJpaRepo.findByBookingId(bookingId);
    }

    @Override
    public void delete(BookingDetail bookingDetail) {
        bookingDetailJpaRepo.delete(bookingDetail);
    }
}
