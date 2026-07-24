package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.SeatStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "seat_availability",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_trip_seat_segment",
                columnNames = {
                        "trip_id",
                        "seat_code",
                        "start_station_order",
                        "end_station_order"
                }
        )
)
public class SeatAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Pattern(
            regexp = "^[A-Z]\\d{2}$",
            message = "seat code not in right format"
    )
    @Column(name = "seat_code", nullable = false, length = 10)
    private String seatCode;

    @NotNull
    @Min(0)
    @Column(name = "start_station_order", nullable = false)
    private Integer startStationOrder;

    @NotNull
    @Min(0)
    @Column(name = "end_station_order", nullable = false)
    private Integer endStationOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    private LocalDateTime holdExpiredAt;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_detail_id")
    private BookingDetail bookingDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public SeatAvailability() {
    }

    @AssertTrue
    public boolean isStationOrderValid() {
        return startStationOrder == null
                || endStationOrder == null
                || startStationOrder < endStationOrder;
    }

    public boolean isHoldExpired(LocalDateTime now) {
        return status == SeatStatus.HELD
                && holdExpiredAt != null
                && !holdExpiredAt.isAfter(now);
    }

    public void hold(
            BookingDetail detail,
            LocalDateTime expiredAt
    ) {
        status = SeatStatus.HELD;
        bookingDetail = detail;
        holdExpiredAt = expiredAt;
    }

    public void markAsBooked() {
        status = SeatStatus.BOOKED;
        holdExpiredAt = null;
    }

    public void release() {
        status = SeatStatus.AVAILABLE;
        holdExpiredAt = null;
        bookingDetail = null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }

    public Integer getStartStationOrder() {
        return startStationOrder;
    }

    public void setStartStationOrder(Integer startStationOrder) {
        this.startStationOrder = startStationOrder;
    }

    public Integer getEndStationOrder() {
        return endStationOrder;
    }

    public void setEndStationOrder(Integer endStationOrder) {
        this.endStationOrder = endStationOrder;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public LocalDateTime getHoldExpiredAt() {
        return holdExpiredAt;
    }

    public void setHoldExpiredAt(LocalDateTime holdExpiredAt) {
        this.holdExpiredAt = holdExpiredAt;
    }

    public Long getVersion() {
        return version;
    }

    public BookingDetail getBookingDetail() {
        return bookingDetail;
    }

    public void setBookingDetail(BookingDetail bookingDetail) {
        this.bookingDetail = bookingDetail;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SeatAvailability that = (SeatAvailability) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}