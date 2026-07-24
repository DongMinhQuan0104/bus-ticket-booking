package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Transaction;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TransactionStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Transaction;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TransactionRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<Transaction> findById(UUID id) {
        return transactionJpaRepo.findById(id);
    }

    @Override
    public Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable) {
        return transactionJpaRepo.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    @Override
    public long countByStatus(TransactionStatus status) {
        return transactionJpaRepo.countByStatus(status);
    }
}
