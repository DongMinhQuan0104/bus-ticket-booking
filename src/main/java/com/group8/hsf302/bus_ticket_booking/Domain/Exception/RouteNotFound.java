package com.group8.hsf302.bus_ticket_booking.Domain.Exception;

public class RouteNotFound extends RuntimeException {
    public RouteNotFound() {
        super("Route not found");
    }
}
