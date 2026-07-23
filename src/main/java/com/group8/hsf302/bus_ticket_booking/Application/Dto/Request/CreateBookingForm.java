package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingType;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateBookingForm {

    @NotNull(message = "trip can not be null")
    private UUID tripId;

    @NotEmpty(message = "must select at least one seat")
    private List<String> seatCodes = new ArrayList<>();

    private List<String> passengerNames = new ArrayList<>();

    private String contactPhone;

    private String contactEmail;

    @NotNull(message = "payment method can not be null")
    private PaymentMethod paymentMethod;

    private BookingType bookingType = BookingType.ONEWAY;

    private String note;

    public CreateBookingForm() {
    }

    public UUID getTripId() {
        return tripId;
    }

    public void setTripId(UUID tripId) {
        this.tripId = tripId;
    }

    public List<String> getSeatCodes() {
        return seatCodes;
    }

    public void setSeatCodes(List<String> seatCodes) {
        this.seatCodes = seatCodes;
    }

    public List<String> getPassengerNames() {
        return passengerNames;
    }

    public void setPassengerNames(List<String> passengerNames) {
        this.passengerNames = passengerNames;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BookingType getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = bookingType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
