package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.DriverTripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.PassengerManifestViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.BookingDetail;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(target = "routeName", source = "route.name")
    @Mapping(target = "busName", source = "bus.busName")
    @Mapping(target = "busLicensePlate", source = "bus.licensePlate")
    @Mapping(target = "totalPassengers", ignore = true)
    DriverTripViewModel toViewModel(Trip trip);

    @Mapping(target = "bookingDetailId", source = "id")
    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "isCheckedIn", source = "isCheckedIn")
    @Mapping(target = "isReturnTicket", source = "returnTicket")
    PassengerManifestViewModel toViewModel(BookingDetail bookingDetail);
}
