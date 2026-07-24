package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Transaction;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Transaction;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TransactionRepo;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepoImpl implements TransactionRepo {

    private final TransactionJpaRepo transactionJpaRepo;

    public TransactionRepoImpl(TransactionJpaRepo transactionJpaRepo) {
        this.transactionJpaRepo = transactionJpaRepo;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionJpaRepo.save(transaction);
    }
}
