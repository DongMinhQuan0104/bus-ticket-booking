package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Payment;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Payment;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.PaymentRepo;
import org.springframework.stereotype.Repository;

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
}
