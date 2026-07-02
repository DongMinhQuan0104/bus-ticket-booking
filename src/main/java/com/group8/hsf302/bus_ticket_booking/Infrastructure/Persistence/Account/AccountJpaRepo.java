package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Account;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountJpaRepo extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);

    Optional<Account> findByPhoneNumber(String phoneNumber);

    Optional<Account> findByEmailOrPhoneNumber(String email, String phoneNumber);

    Optional<Account> findByIdAndStatus(UUID accountId, Status status);
}