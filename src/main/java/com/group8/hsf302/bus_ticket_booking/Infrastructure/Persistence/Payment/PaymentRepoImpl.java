package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Payment;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Payment;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.PaymentRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PaymentRepoImpl implements PaymentRepo {

    private final PaymentJpaRepo paymentJpaRepo;

    public PaymentRepoImpl(PaymentJpaRepo paymentJpaRepo) {
        this.paymentJpaRepo = paymentJpaRepo;
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepo.save(payment);
    }

    @Override
    public List<Payment> findByBookingId(UUID bookingId) {
        return paymentJpaRepo.findByBookingId(bookingId);
    }

    @Override
    public void delete(Payment payment) {
        paymentJpaRepo.delete(payment);
    }
}
