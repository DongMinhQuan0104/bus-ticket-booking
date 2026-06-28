package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepo {
    void delete(Account account);
    Optional<Account> findById(UUID id);
    Optional<Account> findByEmail(String email);
    void save (Account account);
    Optional<Account> findByIdAndStatus(UUID accountId, Status status);
    Optional<Account> findActiveById(UUID accountId);
}
