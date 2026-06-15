package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;

import java.util.Optional;

public interface AccountRepo {
    Optional<Account> findByEmail(String email);
    void save (Account account);
}
