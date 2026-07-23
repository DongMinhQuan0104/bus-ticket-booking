package com.group8.hsf302.bus_ticket_booking.Application.Service.Driver;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.DriverTripResponse;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;

import java.util.List;
import java.util.UUID;

public interface DriverService {
    List<DriverTripResponse> getAssignedTrips(String driverName);
    DriverTripResponse getTripById(UUID tripId);
    DriverTripResponse updateTripStatus(UUID tripId, Status status);
}
