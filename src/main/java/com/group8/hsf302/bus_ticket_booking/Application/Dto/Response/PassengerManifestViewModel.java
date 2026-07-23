package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import java.util.UUID;

public class PassengerManifestViewModel {

    private UUID bookingDetailId;
    private String passengerName;
    private Double ticketPrice;
    private Double luggageWeightKg;
    private Double luggageFee;
    private Boolean isReturnTicket;
    private UUID bookingId;
    private Boolean isCheckedIn;

    public PassengerManifestViewModel() {
    }

    public PassengerManifestViewModel(UUID bookingDetailId, String passengerName, Double ticketPrice, Double luggageWeightKg, Double luggageFee, Boolean isReturnTicket, UUID bookingId, Boolean isCheckedIn) {
        this.bookingDetailId = bookingDetailId;
        this.passengerName = passengerName;
        this.ticketPrice = ticketPrice;
        this.luggageWeightKg = luggageWeightKg;
        this.luggageFee = luggageFee;
        this.isReturnTicket = isReturnTicket;
        this.bookingId = bookingId;
        this.isCheckedIn = isCheckedIn;
    }

    public UUID getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(UUID bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
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

    public Boolean getIsReturnTicket() {
        return isReturnTicket;
    }

    public void setIsReturnTicket(Boolean returnTicket) {
        isReturnTicket = returnTicket;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public Boolean getIsCheckedIn() {
        return isCheckedIn;
    }

    public void setIsCheckedIn(Boolean checkedIn) {
        isCheckedIn = checkedIn;
    }
}
