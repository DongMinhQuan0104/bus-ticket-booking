package com.group8.hsf302.bus_ticket_booking.Application.Service.Account;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;

import java.util.UUID;

public interface AdminService {
    public AccountViewModel createAccount(CreateAccountForm form);
    public boolean deleteAccount(UUID id);
    public boolean updateAccount(AdminUpdateAccountForm form,UUID id);
}
