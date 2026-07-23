package com.group8.hsf302.bus_ticket_booking.Domain.Repository;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository cho SeatAvailability - 1 ban ghi = 1 ghe cua 1 chuyen.
 * Ghe co 3 trang thai: DA DAT (co bookingDetail), DANG GIU TAM (heldUntil con han), HET HAN GIU.
 * <ul>
 *   <li>countUnavailableSeats: dem ghe khong con trong (da dat + dang giu) -> so ghe trong (E1)</li>
 *   <li>findOccupiedSeatCodes: ma ghe bi khoa tren so do ghe (E2)</li>
 *   <li>findTakenSeatCodesByOthers: ghe bi NGUOI KHAC chiem (E3, chong dat trung)</li>
 *   <li>findActiveHolds / deleteHoldsOfAccount: giu ghe tam khi vao thanh toan</li>
 *   <li>deleteExpiredHolds: scheduler don ghe giu qua han</li>
 *   <li>findByBookingId: lay cac ghe cua 1 ve (E4/E5); delete: giai phong khi huy (E5)</li>
 * </ul>
 */
public interface SeatAvailabilityRepo {

    long countUnavailableSeats(UUID tripId, LocalDateTime now);

    List<String> findOccupiedSeatCodes(UUID tripId, LocalDateTime now);

    List<String> findTakenSeatCodesByOthers(UUID tripId, List<String> seatCodes,
                                            LocalDateTime now, UUID accountId);

    List<SeatAvailability> findActiveHolds(UUID tripId, UUID accountId, LocalDateTime now);

    int deleteExpiredHolds(LocalDateTime now);

    int deleteHoldsOfAccount(UUID tripId, UUID accountId);

    List<SeatAvailability> findByBookingId(UUID bookingId);

    SeatAvailability save(SeatAvailability seatAvailability);

    void delete(SeatAvailability seatAvailability);
}
