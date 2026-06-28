package com.group8.hsf302.bus_ticket_booking.Application.Service.Account;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.AccountMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.AccountNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.AccountRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Security.PasswordHasher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService{

    private final AccountRepo accountRepo;
    private final PasswordHasher passwordHasher;
    private final AccountMapper mapper;

    public AdminServiceImpl(AccountRepo accountRepo, PasswordHasher passwordHasher, AccountMapper mapper) {
        this.accountRepo = accountRepo;
        this.passwordHasher = passwordHasher;
        this.mapper = mapper;
    }

    @Override
    public AccountViewModel createAccount(CreateAccountForm form) {
        Account account = mapper.toEntity(form);
        String securedPassword = passwordHasher.hash(account.getPassword());
        account.setPassword(securedPassword);
        accountRepo.save(account);
        return mapper.toViewModel(account);
    }

    @Override
    public boolean deleteAccount(UUID accountId) {
        Account account = findById(accountId);
        accountRepo.delete(account);
        return true;
    }

    @Override
    public boolean updateAccount(AdminUpdateAccountForm form, UUID accountId) {
        Account account = findById(accountId);
        Account updateAccount = mapper.updateEntityFromForm(form, account);
        String securedPassword = passwordHasher.hash(updateAccount.getPassword());
        updateAccount.setPassword(securedPassword);
        accountRepo.save(updateAccount);
        return true;
    }

    private Account findById(UUID accountId) {
        return accountRepo.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }
}
