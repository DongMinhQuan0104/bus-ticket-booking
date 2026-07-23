package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateStationForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.StationViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Station;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StationMapper {

    @Mapping(target = "id", ignore = true)
    Station toEntity(AdminCreateStationForm form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Station updateEntityFromForm(AdminUpdateStationForm form, @MappingTarget Station entity);

    StationViewModel toViewModel(Station station);

}
