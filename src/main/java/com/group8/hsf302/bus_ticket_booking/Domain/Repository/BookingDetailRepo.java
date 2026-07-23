package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.BookingDetail;

import java.util.List;
import java.util.UUID;

public interface BookingDetailRepo {
    BookingDetail save(BookingDetail bookingDetail);
    List<BookingDetail> findByBookingId(UUID bookingId);
    void delete(BookingDetail bookingDetail);
}
