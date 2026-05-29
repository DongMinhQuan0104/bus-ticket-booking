package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Payment;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentJpaRepo extends JpaRepository<Payment, UUID> {
}
