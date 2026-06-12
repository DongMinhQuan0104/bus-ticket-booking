package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.RegisterForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "CUSTOMER")
    Account toEntity(RegisterForm form);

    AccountViewModel toViewModel(Account entity);
}
