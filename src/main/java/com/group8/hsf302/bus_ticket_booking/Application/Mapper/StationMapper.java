package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.StationViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Station;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface StationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "AVAILABLE")
    Station toEntity(CreateStationForm form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Station updateEntityFromForm(UpdateStationForm form, @MappingTarget Station entity);

    StationViewModel toViewModel(Station entity);
}