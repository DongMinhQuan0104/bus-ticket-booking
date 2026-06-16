package com.group8.hsf302.bus_ticket_booking.Application.Service.Account;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    public AccountViewModel create(CreateAccountForm form);
    public AccountViewModel update(UpdateAccountForm form);
    public void delete(UUID accountId);
    public List<AccountViewModel> findAll();
    public AccountViewModel findById(UUID accountId);
}
