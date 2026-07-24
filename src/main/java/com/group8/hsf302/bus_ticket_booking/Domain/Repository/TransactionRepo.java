package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Transaction;

/**
 * Repository cho Transaction. Hien dung de GHI NHAN KHOAN HOAN TIEN khi khach huy ve (E5).
 */
public interface TransactionRepo {
    Transaction save(Transaction transaction);
}
