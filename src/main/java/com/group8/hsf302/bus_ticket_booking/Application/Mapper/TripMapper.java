package com.group8.hsf302.bus_ticket_booking.Application.Mapper;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminCreateTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.AdminUpdateTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.mapstruct.*;

/**
 * Chuyen doi giua Entity Trip va cac DTO bang MapStruct (sinh code luc bien dich).
 * - toViewModel(trip, totalSeats, availableSeats): E1/E2 Customer (so ghe do service tinh san).
 * - toViewModel(trip): Admin (B4) - danh sach chuyen; totalSeats/availableSeats khong dung nen de mac dinh.
 * - toEntity / updateEntityFromForm: Admin tao / sua chuyen tu form.
 */
@Mapper(componentModel = "spring")
public interface TripMapper {

    // ===== Customer (E1/E2): kem so ghe do service tinh =====
    @Mapping(target = "busName", source = "trip.bus.busName")
    @Mapping(target = "busType", source = "trip.bus.busType")
    @Mapping(target = "busCapacity", source = "trip.bus.capacity")
    @Mapping(target = "busLicensePlate", source = "trip.bus.licensePlate")
    @Mapping(target = "status", source = "trip.status")
    @Mapping(target = "routeId", source = "trip.route.id")
    @Mapping(target = "routeName", source = "trip.route.name")
    @Mapping(target = "busId", source = "trip.bus.id")
    TripViewModel toViewModel(Trip trip, int totalSeats, int availableSeats);

    // ===== Admin (B4): danh sach chuyen (khong can so ghe) =====
    @Mapping(target = "busName", source = "bus.busName")
    @Mapping(target = "busType", source = "bus.busType")
    @Mapping(target = "busCapacity", source = "bus.capacity")
    @Mapping(target = "busLicensePlate", source = "bus.licensePlate")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "routeId", source = "route.id")
    @Mapping(target = "routeName", source = "route.name")
    @Mapping(target = "busId", source = "bus.id")
    @Mapping(target = "totalSeats", ignore = true)
    @Mapping(target = "availableSeats", ignore = true)
    TripViewModel toViewModel(Trip trip);

    // ===== Admin: tao chuyen tu form (route/bus gan o service) =====
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "bus", ignore = true)
    Trip toEntity(AdminCreateTripForm form);

    // ===== Admin: sua chuyen tu form (bo qua field null de khong ghi de) =====
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "bus", ignore = true)
    Trip updateEntityFromForm(AdminUpdateTripForm form, @MappingTarget Trip trip);
}
