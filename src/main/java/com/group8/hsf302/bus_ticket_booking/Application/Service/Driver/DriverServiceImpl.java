package com.group8.hsf302.bus_ticket_booking.Application.Service.Driver;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.DriverTripResponse;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.DriverMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.TripNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TripRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {

    private final TripRepo tripRepo;
    private final DriverMapper driverMapper;

    public DriverServiceImpl(TripRepo tripRepo, DriverMapper driverMapper) {
        this.tripRepo = tripRepo;
        this.driverMapper = driverMapper;
    }

    @Override
    public List<DriverTripResponse> getAssignedTrips(String driverName) {
        List<Trip> trips = tripRepo.findByDriverName(driverName);
        return trips.stream()
                .map(driverMapper::toTripResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DriverTripResponse getTripById(UUID tripId) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(TripNotFoundException::new);
        return driverMapper.toTripResponse(trip);
    }

    @Override
    public DriverTripResponse updateTripStatus(UUID tripId, Status status) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(TripNotFoundException::new);
        trip.setStatus(status);
        Trip savedTrip = tripRepo.save(trip);
        return driverMapper.toTripResponse(savedTrip);
    }
}
