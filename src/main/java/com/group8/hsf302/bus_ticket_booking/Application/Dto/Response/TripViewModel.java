package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusCapacity;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BusType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * E1/E2 - Du lieu 1 chuyen xe tra ra giao dien (Response DTO, dang record - bat bien).
 * Khong lo Entity Trip ra ngoai; cac truong busName/busType/busCapacity lay tu Trip.bus qua TripMapper.
 * price (E3) va totalSeats/availableSeats (tinh o service) duoc them de hien gia va so ghe con trong.
 */
public record TripViewModel(
        UUID id,
        String destinationFrom,
        String destinationTo,
        LocalDateTime departureTime,
        String driverName,
        Double price,
        String busName,
        BusType busType,
        BusCapacity busCapacity,
        int totalSeats,
        int availableSeats
) {
    /** Het cho khi khong con ghe trong -> giao dien lam mo card va khoa nut chon. */
    public boolean soldOut() {
        return availableSeats <= 0;
    }
}
