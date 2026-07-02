package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.SeatStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "seat_availability")
public class SeatAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Pattern(regexp = "^[A-Z]\\d{2}$", message = "seat code not in right format")
    @Column(name = "seat_code")
    private String seatCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SeatStatus status;

    @Min(value = 0, message = "start station order can not negative")
    @Column(name = "start_station_order")
    private Integer startStationOrder;

    @Min(value = 0, message = "end station order can not negative")
    @Column(name = "end_station_order")
    private Integer endStationOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_detail_id")
    private BookingDetail bookingDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public SeatAvailability() {
    }

    public SeatAvailability(String seatCode, SeatStatus status, Integer startStationOrder,
                            Integer endStationOrder, BookingDetail bookingDetail, Trip trip) {
        this.seatCode = seatCode;
        this.status = status;
        this.startStationOrder = startStationOrder;
        this.endStationOrder = endStationOrder;
        this.bookingDetail = bookingDetail;
        this.trip = trip;
    }

    public UUID getId() {
        return id;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public Integer getStartStationOrder() {
        return startStationOrder;
    }

    public Integer getEndStationOrder() {
        return endStationOrder;
    }

    public BookingDetail getBookingDetail() {
        return bookingDetail;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public void setStartStationOrder(Integer startStationOrder) {
        this.startStationOrder = startStationOrder;
    }

    public void setEndStationOrder(Integer endStationOrder) {
        this.endStationOrder = endStationOrder;
    }

    public void setBookingDetail(BookingDetail bookingDetail) {
        this.bookingDetail = bookingDetail;
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

    @Override
    public String toString() {
        return "SeatAvailability{" +
                "id=" + id +
                ", seatCode='" + seatCode + '\'' +
                ", status=" + status +
                ", startStationOrder=" + startStationOrder +
                ", endStationOrder=" + endStationOrder +
                '}';
    }
}