package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Transaction;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionJpaRepo extends JpaRepository<Transaction, UUID> {
}
