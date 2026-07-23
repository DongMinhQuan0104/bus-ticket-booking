package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.RouteStation;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.RouteStation;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.RouteStationRepo;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RouteStationRepoImpl
        implements RouteStationRepo {

    private final RouteStationJpaRepo jpaRepo;

    public RouteStationRepoImpl(
            RouteStationJpaRepo jpaRepo
    ) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<RouteStation>
    findByRouteIdAndStationOrder(
            UUID routeId,
            Integer stationOrder
    ) {
        return jpaRepo.findByRoute_IdAndStationOrder(
                routeId,
                stationOrder
        );
    }
}