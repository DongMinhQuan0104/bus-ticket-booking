package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;

import java.util.List;
import java.util.UUID;

/**
 * Repository cho SeatAvailability - ban ghi "1 ghe cua 1 chuyen da duoc dat" (co bookingDetail).
 * Dung xuyen suot luong dat ve:
 * <ul>
 *   <li>countBookedSeats: dem ghe da dat -> tinh ghe con trong (E1)</li>
 *   <li>findOccupiedSeatCodes: danh sach ghe da dat -> khoa tren so do ghe (E2)</li>
 *   <li>findTakenSeatCodes: kiem tra trong so ghe khach chon co ghe nao da bi dat (E3, chong trung)</li>
 *   <li>findByBookingId: lay cac ghe cua 1 ve (E4/E5)</li>
 *   <li>save: giu ghe khi dat (E3); delete: giai phong ghe khi huy (E5)</li>
 * </ul>
 */
public interface SeatAvailabilityRepo {
    long countBookedSeats(UUID tripId);

    List<String> findOccupiedSeatCodes(UUID tripId);

    List<String> findTakenSeatCodes(UUID tripId, List<String> seatCodes);

    List<SeatAvailability> findByBookingId(UUID bookingId);

    SeatAvailability save(SeatAvailability seatAvailability);

    void delete(SeatAvailability seatAvailability);
}
