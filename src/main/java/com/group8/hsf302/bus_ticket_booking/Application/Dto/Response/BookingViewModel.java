package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * E4 - Du lieu 1 ve da dat de hien o trang "Ve cua toi" (va E5/E6).
 * Vi Booking khong tro truc tiep toi Trip nen thong tin chuyen (tuyen/gio/nha xe) duoc gom san vao day.
 * completed = chuyen da khoi hanh -> quyet dinh canReview (E6) va canCancel (E5).
 */
public record BookingViewModel(
        UUID id,
        LocalDateTime dateBooked,
        Double totalPrice,
        BookingType bookingType,
        String note,
        UUID tripId,
        String destinationFrom,
        String destinationTo,
        LocalDateTime departureTime,
        String busName,
        String driverName,
        List<String> seatCodes,
        List<String> passengerNames,
        boolean completed
) {
    // Chuyen da khoi hanh -> co the danh gia (E6); chua khoi hanh -> co the huy (E5)
    public boolean canReview() {
        return completed;
    }

    public boolean canCancel() {
        return !completed;
    }
}
