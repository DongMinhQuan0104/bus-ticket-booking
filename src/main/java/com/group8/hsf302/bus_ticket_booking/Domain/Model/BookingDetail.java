package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "booking_detail")
public class BookingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "passenger name can not blank")
    @Column(nullable = false)
    private String passengerName;

    @NotBlank(message = "seat code can not blank")
    @Column(nullable = false, length = 10)
    private String seatCode;

    @Min(value = 0, message = "ticket price can not negative")
    @Column(nullable = false)
    private Double ticketPrice;

    @Min(value = 0, message = "luggage weight can not negative")
    private Double luggageWeightKg;

    @Min(value = 0, message = "luggage fee can not negative")
    private Double luggageFee;

    @Min(value = 0, message = "sub total can not negative")
    @Column(nullable = false)
    private Double subTotal;

    @Column(nullable = false)
    private Boolean isReturnTicket = false;

    @NotNull(message = "pickup station order can not null")
    @Min(value = 0)
    @Column(nullable = false)
    private Integer pickupStationOrder;

    @NotNull(message = "dropoff station order can not null")
    @Min(value = 0)
    @Column(nullable = false)
    private Integer dropoffStationOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public BookingDetail() {
    }

    @AssertTrue(message = "dropoff station must be after pickup station")
    public boolean isStationOrderValid() {
        return pickupStationOrder == null
                || dropoffStationOrder == null
                || pickupStationOrder < dropoffStationOrder;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Double getLuggageWeightKg() {
        return luggageWeightKg;
    }

    public void setLuggageWeightKg(Double luggageWeightKg) {
        this.luggageWeightKg = luggageWeightKg;
    }

    public Double getLuggageFee() {
        return luggageFee;
    }

    public void setLuggageFee(Double luggageFee) {
        this.luggageFee = luggageFee;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Boolean getReturnTicket() {
        return isReturnTicket;
    }

    public void setReturnTicket(Boolean returnTicket) {
        isReturnTicket = returnTicket;
    }

    public Integer getPickupStationOrder() {
        return pickupStationOrder;
    }

    public void setPickupStationOrder(Integer pickupStationOrder) {
        this.pickupStationOrder = pickupStationOrder;
    }

    public Integer getDropoffStationOrder() {
        return dropoffStationOrder;
    }

    public void setDropoffStationOrder(Integer dropoffStationOrder) {
        this.dropoffStationOrder = dropoffStationOrder;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookingDetail that = (BookingDetail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}