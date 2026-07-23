package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.BookingDetail;

import java.util.List;
import java.util.UUID;

/**
 * Repository cho BookingDetail (chi tiet ve cua tung hanh khach/ghe trong 1 don).
 * save: tao khi dat ve (E3); findByBookingId: liet ke hanh khach (E4); delete: khi huy ve (E5).
 */
public interface BookingDetailRepo {
    BookingDetail save(BookingDetail bookingDetail);
    List<BookingDetail> findByBookingId(UUID bookingId);
    void delete(BookingDetail bookingDetail);
}
