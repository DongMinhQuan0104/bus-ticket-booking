package com.group8.hsf302.bus_ticket_booking.Application.Service.Customer;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Mapper.TripMapper;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SameStationException;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.SeatAvailabilityRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TripRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerSearchTripServiceImpl implements CustomerSearchTripService {

    private final TripRepo tripRepo;
    private final SeatAvailabilityRepo seatAvailabilityRepo;
    private final TripMapper tripMapper;

    public CustomerSearchTripServiceImpl(TripRepo tripRepo,
                                         SeatAvailabilityRepo seatAvailabilityRepo,
                                         TripMapper tripMapper) {
        this.tripRepo = tripRepo;
        this.seatAvailabilityRepo = seatAvailabilityRepo;
        this.tripMapper = tripMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripViewModel> search(SearchTripForm form) {
        String from = form.getDestinationFrom().trim();
        String to = form.getDestinationTo().trim();

        // BE khong tin FE: kiem tra lai nghiep vu tai tang service
        if (from.equalsIgnoreCase(to)) {
            throw new SameStationException();
        }

        LocalDate date = form.getDepartureDate();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Trip> trips = tripRepo.searchAvailable(from, to, startOfDay, endOfDay);

        List<TripViewModel> result = new ArrayList<>();
        for (Trip trip : trips) {
            int totalSeats = totalSeatsOf(trip);
            long booked = seatAvailabilityRepo.countBookedSeats(trip.getId());
            int available = totalSeats - (int) booked;
            if (available < 0) {
                available = 0;
            }
            result.add(tripMapper.toViewModel(trip, totalSeats, available));
        }
        return result;
    }

    private int totalSeatsOf(Trip trip) {
        if (trip.getBus() == null || trip.getBus().getCapacity() == null) {
            return 0;
        }
        BusCapacity capacity = trip.getBus().getCapacity();
        if (capacity == BusCapacity.SEAT_16) {
            return 16;
        }
        return 0;
    }
}
