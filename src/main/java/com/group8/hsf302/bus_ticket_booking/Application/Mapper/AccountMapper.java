package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.RegisterForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "CUSTOMER")
    @Mapping(target = "status", constant = "AVAILABLE")
    Account toEntity(RegisterForm form);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status",constant = "AVAILABLE")
    Account toEntity(CreateAccountForm form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "password", ignore = true)
    Account updateEntityFromForm(UpdateAccountForm form, @MappingTarget Account entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Account updateEntityFromForm(AdminUpdateAccountForm form, @MappingTarget Account entity);

    AccountViewModel toViewModel(Account entity);

}
