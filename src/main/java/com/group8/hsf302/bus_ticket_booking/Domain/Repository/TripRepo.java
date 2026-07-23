package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepo {
    List<Trip> searchAvailable(String destinationFrom, String destinationTo,
                               LocalDateTime startOfDay, LocalDateTime endOfDay);
}
