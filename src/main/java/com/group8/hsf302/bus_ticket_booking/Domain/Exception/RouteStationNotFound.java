package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class RouteStationNotFound extends RuntimeException {
    public RouteStationNotFound() {
        super("Route Station Not Found");
    }
}
