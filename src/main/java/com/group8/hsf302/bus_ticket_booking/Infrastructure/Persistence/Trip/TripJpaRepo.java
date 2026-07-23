package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.Trip;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TripJpaRepo extends JpaRepository<Trip, UUID> {

    // ===== Customer (Viet) - E1: tim chuyen theo trang thai + diem di/den + khoang ngay di =====
    // Truong "status" cua Trip nay la kieu TripStatus (thong nhat 1 truong). Khach chi thay chuyen SCHEDULED.
    List<Trip> findByStatusAndDestinationFromIgnoreCaseAndDestinationToIgnoreCaseAndDepartureTimeBetweenOrderByDepartureTimeAsc(
            TripStatus status, String destinationFrom, String destinationTo,
            LocalDateTime start, LocalDateTime end);

    // ===== Driver (An) =====
    List<Trip> findByDriverName(String driverName);
}
