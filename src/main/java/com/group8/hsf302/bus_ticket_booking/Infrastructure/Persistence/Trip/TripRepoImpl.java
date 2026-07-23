package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.TripRepo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TripRepoImpl implements TripRepo {

    private final TripJpaRepo tripJpaRepo;

    public TripRepoImpl(TripJpaRepo tripJpaRepo) {
        this.tripJpaRepo = tripJpaRepo;
    }

    @Override
    public List<Trip> searchAvailable(String destinationFrom, String destinationTo,
                                      LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return tripJpaRepo
                .findByStatusAndDestinationFromIgnoreCaseAndDestinationToIgnoreCaseAndDepartureTimeBetweenOrderByDepartureTimeAsc(
                        Status.AVAILABLE, destinationFrom, destinationTo, startOfDay, endOfDay);
    }
}
