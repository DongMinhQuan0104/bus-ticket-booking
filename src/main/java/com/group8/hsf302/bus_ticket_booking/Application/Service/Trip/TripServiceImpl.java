package com.group8.hsf302.bus_ticket_booking.Application.Service.Trip;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.SeatStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.SeatAvailabilityRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TripRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TripServiceImpl implements TripService {

    private static final int TOTAL_SEATS = 34;

    private final TripRepo tripRepo;
    private final SeatAvailabilityRepo seatAvailabilityRepo;

    public TripServiceImpl(TripRepo tripRepo, SeatAvailabilityRepo seatAvailabilityRepo) {
        this.tripRepo = tripRepo;
        this.seatAvailabilityRepo = seatAvailabilityRepo;
    }

    @Override
    @Transactional
    public TripViewModel createTrip(CreateTripForm form) {

        Trip trip = new Trip();
        trip.setDestinationFrom(form.getDestinationFrom().trim());
        trip.setDestinationTo(form.getDestinationTo().trim());
        trip.setDepartureTime(form.getDepartureTime());
        trip.setArrivalTime(form.getArrivalTime());
        trip.setBusCode(form.getBusCode().trim());
        trip.setBusLicensePlate(form.getBusLicensePlate().trim());
        trip.setVehicleType(form.getVehicleType().trim());
        trip.setDriverName(form.getDriverName().trim());
        trip.setPrice(form.getPrice());
        trip.setTotalSeats(TOTAL_SEATS);
        trip.setAvailableSeats(TOTAL_SEATS);
        trip.setStatus(TripStatus.SCHEDULED);

        Trip savedTrip = tripRepo.save(trip);

        List<SeatAvailability> seats = createDefaultSeats(savedTrip);
        seatAvailabilityRepo.saveAll(seats);

        return toViewModel(savedTrip);
    }

    @Override
    public List<TripViewModel> getAllTrips() {
        return tripRepo.findAll()
                .stream()
                .map(this::toViewModel)
                .toList();
    }

    @Override
    public TripViewModel getTripById(UUID tripId) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        return toViewModel(trip);
    }

    private List<SeatAvailability> createDefaultSeats(Trip trip) {
        List<SeatAvailability> seats = new ArrayList<>();

        for (int i = 1; i <= 17; i++) {
            SeatAvailability seat = new SeatAvailability();
            seat.setSeatCode("A" + String.format("%02d", i));
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setStartStationOrder(0);
            seat.setEndStationOrder(0);
            seat.setTrip(trip);
            seats.add(seat);
        }

        for (int i = 1; i <= 17; i++) {
            SeatAvailability seat = new SeatAvailability();
            seat.setSeatCode("B" + String.format("%02d", i));
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setStartStationOrder(0);
            seat.setEndStationOrder(0);
            seat.setTrip(trip);
            seats.add(seat);
        }

        return seats;
    }

    private TripViewModel toViewModel(Trip trip) {
        return new TripViewModel(
                trip.getId(),
                trip.getDestinationFrom(),
                trip.getDestinationTo(),
                trip.getDepartureTime(),
                trip.getArrivalTime(),
                trip.getBusCode(),
                trip.getBusLicensePlate(),
                trip.getVehicleType(),
                trip.getDriverName(),
                trip.getPrice(),
                trip.getTotalSeats(),
                trip.getAvailableSeats(),
                trip.getStatus()
        );
    }
}