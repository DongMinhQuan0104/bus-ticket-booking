package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository (interface o tang Domain) cho Trip. Cai dat that o Infrastructure (TripRepoImpl).
 * searchAvailable: phuc vu E1 (tim chuyen AVAILABLE theo diem di/den + khoang thoi gian ngay di).
 * findById: phuc vu E2/E3 (lay chuyen de chon ghe / dat ve).
 */
public interface TripRepo {
    List<Trip> searchAvailable(String destinationFrom, String destinationTo,
                               LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Trip> findById(UUID id);
}
