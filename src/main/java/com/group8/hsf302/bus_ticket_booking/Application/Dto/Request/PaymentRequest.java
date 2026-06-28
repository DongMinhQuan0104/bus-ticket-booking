package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.PaymentMethod;
import java.util.UUID;

public class PaymentRequest {

    private UUID bookingId;
    private PaymentMethod paymentMethod;

    public PaymentRequest() {
    }

    public PaymentRequest(UUID bookingId, PaymentMethod paymentMethod) {
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
