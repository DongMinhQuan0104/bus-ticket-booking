package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository (interface o tang Domain) cho Trip. Cai dat that o Infrastructure (TripRepoImpl).
 * searchAvailable: phuc vu E1 (tim chuyen con dat duoc theo diem di/den + khoang thoi gian ngay di).
 * findById: phuc vu E2/E3 (lay chuyen de chon ghe / dat ve).
 * findByDriverName / save: phuc vu nghiep vu Driver (An).
 */
public interface TripRepo {
    // ===== Customer (Viet) =====
    List<Trip> searchAvailable(String destinationFrom, String destinationTo,
                               LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Trip> findById(UUID id);

    // ===== Driver (An) =====
    List<Trip> findByDriverName(String driverName);

    Trip save(Trip trip);

    // ===== Admin (B4 - quan ly chuyen) =====
    Page<Trip> findAll(Pageable pageable);

    void deleteById(UUID id);
}
