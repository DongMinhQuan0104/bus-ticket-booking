package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Payment;

public interface PaymentRepo {
    Payment save(Payment payment);
}
