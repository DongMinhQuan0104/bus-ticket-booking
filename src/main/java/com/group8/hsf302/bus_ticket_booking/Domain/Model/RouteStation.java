package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "route_station")
public class RouteStation {

    @Id
    private UUID id;

    private Integer stationOrder;

    private Double priceFromStart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

}
