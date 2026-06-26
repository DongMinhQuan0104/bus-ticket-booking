package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Payment;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Payment;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentRepoImpl implements PaymentRepo {

    @Autowired
    private PaymentJpaRepo paymentJpaRepo;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepo.save(payment);
    }
}
