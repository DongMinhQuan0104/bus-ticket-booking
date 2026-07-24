package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

public record StaffRouteStationViewModel(
        Integer stationOrder,
        String stationName,
        String address,
        Double priceFromStart
) {
}