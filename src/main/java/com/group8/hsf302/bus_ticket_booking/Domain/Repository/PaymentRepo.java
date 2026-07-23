package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentRepo {
    Payment save(Payment payment);
    List<Payment> findByBookingId(UUID bookingId);
    void delete(Payment payment);
}
