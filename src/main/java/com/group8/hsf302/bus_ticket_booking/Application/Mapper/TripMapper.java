package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "bus", ignore = true)
    Trip toEntity(AdminCreateTripForm form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "bus", ignore = true)
    Trip updateEntityFromForm(AdminUpdateTripForm form,@MappingTarget Trip trip);

    @Mapping(source = "route.id", target = "routeId")
    @Mapping(source = "route.name", target = "routeName")
    @Mapping(source = "bus.id", target = "busId")
    @Mapping(source = "bus.licensePlate", target = "busLicensePlate")
    TripViewModel toViewModel(Trip trip);
}
