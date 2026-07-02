package com.group8.hsf302.bus_ticket_booking.Application.Service.Authentication;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.LoginForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.RegisterForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.AccountMapper;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Authentication.AuthenticationService;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
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
        String fullName = form.getFullName().trim();
        String email = form.getEmail().trim().toLowerCase();
        String phoneNumber = form.getPhoneNumber().trim();
        String gender = form.getGender().trim().toUpperCase();

        if (accountRepo.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        if (accountRepo.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new PhoneNumberAlreadyExistsException();
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new PasswordConfirmNotMatchException();
        }

        form.setFullName(fullName);
        form.setEmail(email);
        form.setPhoneNumber(phoneNumber);
        form.setGender(gender);

        Account account = accountMapper.toEntity(form);

        String securedPassword = passwordHasher.hash(form.getPassword());
        account.setPassword(securedPassword);

        accountRepo.save(account);

        return accountMapper.toViewModel(account);
    }

    @Override
    public AccountViewModel login(LoginForm form) {

        String phoneNumber = form.getPhoneNumber().trim();

        Account account = accountRepo
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(InvalidCredentialsException::new);

        if (account.getStatus() != Status.AVAILABLE) {
            throw new InvalidCredentialsException();
        }

        if (!passwordHasher.verify(form.getPassword(), account.getPassword())) {
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
