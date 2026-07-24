package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository cho Booking (don dat ve). save: tao ve (E3); findByAccountId: ve cua toi (E4);
 * findById: lay 1 ve de xem/huy/danh gia (E4/E5/E6); delete: huy ve (E5).
 */
public interface BookingRepo {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID id);
    List<Booking> findByAccountId(UUID accountId);
    void delete(Booking booking);
}
