package com.group8.hsf302.bus_ticket_booking.Application.Service.Account;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.AccountMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.AccountRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepo accountRepo, AccountMapper accountMapper) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountViewModel create(CreateAccountForm form) {
        return null;
    }

    @Override
    public AccountViewModel update(UpdateAccountForm form) {
        return null;
    }

    @Override
    public void delete(UUID accountId) {

    }

    @Override
    public List<AccountViewModel> findAll() {
        return List.of();
    }

    @Override
    public AccountViewModel findById(UUID accountId) {
        return null;
    }
}
