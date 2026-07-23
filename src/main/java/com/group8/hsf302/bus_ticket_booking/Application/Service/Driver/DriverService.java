package com.group8.hsf302.bus_ticket_booking.Application.Service.Driver;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.DriverTripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.PassengerManifestViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;

import java.util.List;
import java.util.UUID;

public interface DriverService {
    List<DriverTripViewModel> getAssignedTrips(String driverName);
    DriverTripViewModel getTripById(UUID tripId);
    DriverTripViewModel updateTripStatus(UUID tripId, Status status);
    List<PassengerManifestViewModel> getPassengerManifest(UUID tripId);
    PassengerManifestViewModel checkInPassenger(UUID bookingDetailId);
}
