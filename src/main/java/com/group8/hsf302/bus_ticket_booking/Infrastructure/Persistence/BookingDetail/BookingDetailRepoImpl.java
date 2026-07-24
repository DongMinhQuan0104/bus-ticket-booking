package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.BookingDetail;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.BookingDetail;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingDetailRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookingDetailRepoImpl
        implements BookingDetailRepo {

    private final BookingDetailJpaRepo bookingDetailJpaRepo;

    public BookingDetailRepoImpl(
            BookingDetailJpaRepo bookingDetailJpaRepo
    ) {
        this.bookingDetailJpaRepo = bookingDetailJpaRepo;
    }

    @Override
    public BookingDetail save(
            BookingDetail bookingDetail
    ) {
        return bookingDetailJpaRepo.save(
                bookingDetail
        );
    }

    @Override
    public List<BookingDetail> saveAll(
            List<BookingDetail> bookingDetails
    ) {
        return bookingDetailJpaRepo.saveAll(
                bookingDetails
        );
    }

    @Override
    public Optional<BookingDetail> findById(
            UUID id
    ) {
        return bookingDetailJpaRepo.findById(id);
    }

    @Override
    public List<BookingDetail> findByBookingId(
            UUID bookingId
    ) {
        return bookingDetailJpaRepo
                .findByBooking_Id(bookingId);
    }
}