package com.group8.hsf302.bus_ticket_booking.Application.Service.Driver;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.DriverTripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.PassengerManifestViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.DriverMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.AlreadyCheckedInException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.BookingDetailNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.TripNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.BookingDetail;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingDetailRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TripRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {

    private final TripRepo tripRepo;
    private final BookingDetailRepo bookingDetailRepo;
    private final DriverMapper driverMapper;

    public DriverServiceImpl(TripRepo tripRepo, BookingDetailRepo bookingDetailRepo, DriverMapper driverMapper) {
        this.tripRepo = tripRepo;
        this.bookingDetailRepo = bookingDetailRepo;
        this.driverMapper = driverMapper;
    }

    @Override
    public List<DriverTripViewModel> getAssignedTrips(String driverName) {
        List<Trip> trips = tripRepo.findByDriverName(driverName);
        return trips.stream()
                .map(driverMapper::toViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public DriverTripViewModel getTripById(UUID tripId) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(TripNotFoundException::new);
        return driverMapper.toViewModel(trip);
    }

    @Override
    public DriverTripViewModel updateTripStatus(UUID tripId, TripStatus status) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        // Constraint 1: Driver can only mark trip as READY within 1 hour before departure time
        if (status == TripStatus.READY) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime allowedStartTime = trip.getDepartureTime() != null ? trip.getDepartureTime().minusHours(1) : now;
            if (now.isBefore(allowedStartTime)) {
                throw new IllegalStateException("Driver can only mark READY within 1 hour before departure time.");
            }

            // Constraint 2: Driver can only have 1 active trip (READY or RUNNING) at a time
            List<Trip> assignedTrips = tripRepo.findByDriverName(trip.getDriverName());
            boolean hasActiveOtherTrip = assignedTrips.stream()
                    .anyMatch(t -> !t.getId().equals(tripId) && (t.getStatus() == TripStatus.READY || t.getStatus() == TripStatus.RUNNING));
            if (hasActiveOtherTrip) {
                throw new IllegalStateException("Driver already has another active trip in READY or RUNNING status. Only 1 active trip is allowed at a time.");
            }
        }

        trip.setStatus(status);
        Trip savedTrip = tripRepo.save(trip);
        return driverMapper.toViewModel(savedTrip);
    }

    @Override
    public List<PassengerManifestViewModel> getPassengerManifest(UUID tripId) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        // Constraint 3: Manifest list is locked until driver marks trip as READY, RUNNING, or COMPLETED
        if (trip.getStatus() == TripStatus.SCHEDULED) {
            throw new IllegalStateException("Passenger manifest is locked until driver marks trip status as READY or RUNNING.");
        }

        List<BookingDetail> bookingDetails = bookingDetailRepo.findAll();
        return bookingDetails.stream()
                .map(driverMapper::toViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public PassengerManifestViewModel checkInPassenger(UUID bookingDetailId) {
        BookingDetail bookingDetail = bookingDetailRepo.findById(bookingDetailId)
                .orElseThrow(BookingDetailNotFoundException::new);
        if (Boolean.TRUE.equals(bookingDetail.getIsCheckedIn())) {
            throw new AlreadyCheckedInException();
        }
        bookingDetail.setIsCheckedIn(true);
        BookingDetail savedDetail = bookingDetailRepo.save(bookingDetail);
        return driverMapper.toViewModel(savedDetail);
    }
}
