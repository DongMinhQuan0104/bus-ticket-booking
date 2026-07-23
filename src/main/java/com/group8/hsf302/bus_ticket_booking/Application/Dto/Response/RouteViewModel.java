package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import java.util.List;
import java.util.UUID;

public record RouteViewModel(
        UUID id,
        String name,
        List<RouteStationViewModel> stations
) {
}
