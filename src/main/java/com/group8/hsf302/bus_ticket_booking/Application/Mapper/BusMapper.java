package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateBusForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateBusForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.BusViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Bus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusMapper {

    @Mapping(target = "id", ignore = true)
    Bus toEntity(AdminCreateBusForm createBusForm);

    @Mapping(target = "id", ignore = true)
    Bus toEntity(AdminUpdateBusForm adminUpdateBusForm);

    BusViewModel toViewModel(Bus bus);
}
