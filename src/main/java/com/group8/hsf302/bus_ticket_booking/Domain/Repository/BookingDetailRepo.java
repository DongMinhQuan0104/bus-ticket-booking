package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.BookingDetail;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingDetailRepo {

    BookingDetail save(BookingDetail detail);

    List<BookingDetail> saveAll(
            List<BookingDetail> details
    );

    Optional<BookingDetail> findById(UUID id);

    List<BookingDetail> findByBookingId(
            UUID bookingId
    );
}