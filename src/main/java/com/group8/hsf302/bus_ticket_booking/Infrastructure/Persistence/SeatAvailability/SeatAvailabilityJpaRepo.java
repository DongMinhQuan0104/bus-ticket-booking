package com.group8.hsf302.bus_ticket_booking.Infrastructure.Persistence.SeatAvailability;

import com.group8.hsf302.bus_ticket_booking.Domain.Model.SeatAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Ghe "khong con chon duoc" = DA DAT (bookingDetail is not null)
 * HOAC DANG GIU TAM chua het han (bookingDetail is null and heldUntil > now).
 */
@Repository
public interface SeatAvailabilityJpaRepo extends JpaRepository<SeatAvailability, UUID> {

    long countByTripIdAndBookingDetailIsNotNull(UUID tripId);

    /** Dem ghe khong con trong (da dat + dang giu tam) -> tinh so ghe con lai (E1). */
    @Query("select count(s) from SeatAvailability s " +
           "where s.trip.id = :tripId " +
           "and (s.bookingDetail is not null or s.heldUntil > :now)")
    long countUnavailableSeats(@Param("tripId") UUID tripId, @Param("now") LocalDateTime now);

    /** Ma ghe khong con trong -> khoa tren so do ghe (E2). */
    @Query("select s.seatCode from SeatAvailability s " +
           "where s.trip.id = :tripId " +
           "and (s.bookingDetail is not null or s.heldUntil > :now)")
    List<String> findOccupiedSeatCodes(@Param("tripId") UUID tripId, @Param("now") LocalDateTime now);

    /**
     * Trong so ghe khach chon, ghe nao da bi NGUOI KHAC chiem (dat hoac dang giu tam).
     * Bo qua ghe do CHINH khach dang giu -> khach van xac nhan dat tiep duoc.
     */
    @Query("select s.seatCode from SeatAvailability s " +
           "where s.trip.id = :tripId and s.seatCode in :seatCodes " +
           "and (s.bookingDetail is not null " +
           "     or (s.heldUntil > :now and s.heldByAccountId <> :accountId))")
    List<String> findTakenSeatCodesByOthers(@Param("tripId") UUID tripId,
                                            @Param("seatCodes") List<String> seatCodes,
                                            @Param("now") LocalDateTime now,
                                            @Param("accountId") UUID accountId);

    /** Cac ghe khach nay dang giu tam (con han) cho 1 chuyen. */
    @Query("select s from SeatAvailability s " +
           "where s.trip.id = :tripId and s.bookingDetail is null " +
           "and s.heldByAccountId = :accountId and s.heldUntil > :now")
    List<SeatAvailability> findActiveHolds(@Param("tripId") UUID tripId,
                                           @Param("accountId") UUID accountId,
                                           @Param("now") LocalDateTime now);

    /** Xoa cac ghe giu tam DA HET HAN (scheduler goi dinh ky). */
    @Modifying
    @Query("delete from SeatAvailability s where s.bookingDetail is null and s.heldUntil <= :now")
    int deleteExpiredHolds(@Param("now") LocalDateTime now);

    /** Xoa cac ghe khach nay dang giu cho 1 chuyen (khi chon lai ghe khac). */
    @Modifying
    @Query("delete from SeatAvailability s where s.trip.id = :tripId " +
           "and s.bookingDetail is null and s.heldByAccountId = :accountId")
    int deleteHoldsOfAccount(@Param("tripId") UUID tripId, @Param("accountId") UUID accountId);

    List<SeatAvailability> findByBookingDetailBookingId(UUID bookingId);
}
