package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateBusForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateBusForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.BusViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Bus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusMapper {

    @Mapping(target = "id", ignore = true)
    Bus toEntity(CreateBusForm createBusForm);

    @Mapping(target = "id", ignore = true)
    Bus toEntity(UpdateBusForm updateBusForm);

    BusViewModel toViewModel(Bus bus);
}
