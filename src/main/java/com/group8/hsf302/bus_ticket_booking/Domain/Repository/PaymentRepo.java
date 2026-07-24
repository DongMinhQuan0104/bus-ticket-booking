package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Payment;

import java.util.List;
import java.util.UUID;

/**
 * Repository cho Payment (ban ghi thanh toan cua 1 don). save: tao khi dat ve (E3);
 * findByBookingId + delete: xoa thanh toan khi huy ve (E5).
 */
public interface PaymentRepo {
    Payment save(Payment payment);
    List<Payment> findByBookingId(UUID bookingId);
    void delete(Payment payment);
}
