package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateRouteForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateRouteForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.RouteViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Route;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    @Mapping(target = "id", ignore = true)
    Route toEntity(AdminCreateRouteForm form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Route updateEntityFromForm(AdminUpdateRouteForm form, @MappingTarget Route entity);

    RouteViewModel toViewModel(Route route);
}
