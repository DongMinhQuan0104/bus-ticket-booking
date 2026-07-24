package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.RouteStation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RouteStationRepo {

    List<RouteStation> findByRouteId(UUID routeId);

    Optional<RouteStation> findByRouteIdAndStationOrder(
            UUID routeId,
            Integer stationOrder
    );
}