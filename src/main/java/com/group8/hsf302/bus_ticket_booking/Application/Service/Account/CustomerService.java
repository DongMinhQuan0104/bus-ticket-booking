package com.group8.hsf302.bus_ticket_booking.Application.Service.Account;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.ChangePasswordForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;

import java.util.UUID;

public interface CustomerService {
    public AccountViewModel getAccount(UUID accountId);
    public AccountViewModel update(UpdateAccountForm form,UUID  accountId);
    public boolean changePassword(ChangePasswordForm form,UUID accountId);
    public boolean deleted(UUID accountId);
}
