package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateRouteForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminRouteStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateRouteForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.RouteStationViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.RouteViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Route;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.RouteStation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "routeStations", ignore = true)
    Route toEntity(AdminCreateRouteForm form);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "station", ignore = true)
    RouteStation toRouteStationEntity(AdminRouteStationForm form);

    @Mapping(source = "routeStations", target = "stations")
    RouteViewModel toViewModel(Route route);

    @Mapping(source = "station.id", target = "stationId")
    @Mapping(source = "station.name", target = "stationName")
    RouteStationViewModel toRouteStationViewModel(RouteStation routeStation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "routeStations", ignore = true)
    Route updateEntityFromForm(AdminUpdateRouteForm form, @MappingTarget Route entity);
}
