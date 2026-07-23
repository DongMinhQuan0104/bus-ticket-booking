package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.RouteStation;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.RouteStation;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.RouteStationRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RouteStationRepoImpl implements RouteStationRepo {

    private final RouteStationJpaRepo routeStationJpaRepo;

    public RouteStationRepoImpl(
            RouteStationJpaRepo routeStationJpaRepo
    ) {
        this.routeStationJpaRepo = routeStationJpaRepo;
    }

    @Override
    public List<RouteStation> findByRouteId(UUID routeId) {
        return routeStationJpaRepo
                .findByRouteIdOrderByStationOrderAsc(routeId);
    }

    @Override
    public Optional<RouteStation> findByRouteIdAndStationOrder(
            UUID routeId,
            Integer stationOrder
    ) {
        return routeStationJpaRepo
                .findByRoute_IdAndStationOrder(
                        routeId,
                        stationOrder
                );
    }
}