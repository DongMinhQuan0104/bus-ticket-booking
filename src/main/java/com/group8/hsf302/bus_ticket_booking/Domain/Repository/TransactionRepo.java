package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TransactionStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository cho Transaction. Ghi nhan khoan HOAN TIEN khi khach huy ve (E5)
 * va phuc vu Admin DUYET hoan tien (PENDING -> PAID).
 */
public interface TransactionRepo {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(UUID id);

    Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable);

    long countByStatus(TransactionStatus status);
}
