package com.group8.hsf302.bus_ticket_booking.Application.Service.Authentication;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.LoginForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.RegisterForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.AccountMapper;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Authentication.AuthenticationService;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.AccountRepo;
import com.group8.hsf302.bus_ticket_booking.Infrastructure.Security.PasswordHasher;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;
    private final PasswordHasher passwordHasher;

    public AuthenticationServiceImpl(AccountRepo accountRepo, AccountMapper accountMapper, PasswordHasher passwordHasher) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public AccountViewModel register(RegisterForm form) {
        if(accountRepo.findByEmail(form.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        if(!form.getPassword().equals(form.getConfirmPassword())) {
            throw new PasswordConfirmNotMatchException();
        }
        Account account = accountMapper.toEntity(form);
        String securedPassword = passwordHasher.hash(account.getPassword());
        account.setPassword(securedPassword);
        accountRepo.save(account);
        return accountMapper.toViewModel(account);
    }

    @Override
    public AccountViewModel login(LoginForm form) {
        Account account = accountRepo.findByEmail(form.getEmail()).orElseThrow(InvalidCredentialsException::new);
        if(!passwordHasher.verify(form.getPassword(), account.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return accountMapper.toViewModel(account);
    }

    @Override
    public boolean forgotPassword() {
        return false;
    }

    @Override
    public boolean changePassword() {
        return false;
    }
}
