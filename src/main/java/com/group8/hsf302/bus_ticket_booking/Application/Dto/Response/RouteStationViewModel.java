package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;


import java.util.UUID;

public record RouteStationViewModel(
         UUID id,
         Integer stationOrder,
         Double priceFromStar,
         UUID stationId,
         String stationName
) {}
