package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * E1/E2 - Chuyen doi Entity Trip -> TripViewModel bang MapStruct (sinh code luc bien dich).
 * totalSeats/availableSeats duoc service tinh san va truyen vao (khong co san tren Entity);
 * cac truong ve xe lay tu quan he trip.bus.
 */
@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target = "busName", source = "trip.bus.busName")
    @Mapping(target = "busType", source = "trip.bus.busType")
    @Mapping(target = "busCapacity", source = "trip.bus.capacity")
    TripViewModel toViewModel(Trip trip, int totalSeats, int availableSeats);
}
