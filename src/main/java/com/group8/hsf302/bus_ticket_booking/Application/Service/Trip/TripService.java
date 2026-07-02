package com.group8.hsf302.bus_ticket_booking.Application.Service.Trip;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;

import java.util.List;
import java.util.UUID;

public interface TripService {

    TripViewModel createTrip(CreateTripForm form);

    List<TripViewModel> getAllTrips();

    TripViewModel getTripById(UUID tripId);
}