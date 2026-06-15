package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Account;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.AccountRepo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepoImpl implements AccountRepo {

    private final AccountJpaRepo accountJpaRepo;

    public AccountRepoImpl(AccountJpaRepo accountJpaRepo) {
        this.accountJpaRepo = accountJpaRepo;
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountJpaRepo.findByEmail(email);
    }

    @Override
    public void save(Account account) {
        accountJpaRepo.save(account);
    }
}
