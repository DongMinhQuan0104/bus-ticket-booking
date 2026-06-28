package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.PaymentMethod;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentResponse {

    private UUID paymentId;
    private LocalDateTime createPayment;
    private PaymentMethod paymentMethod;
    private UUID bookingId;

    public PaymentResponse() {
    }

    public PaymentResponse(UUID paymentId, LocalDateTime createPayment, PaymentMethod paymentMethod, UUID bookingId) {
        this.paymentId = paymentId;
        this.createPayment = createPayment;
        this.paymentMethod = paymentMethod;
        this.bookingId = bookingId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getCreatePayment() {
        return createPayment;
    }

    public void setCreatePayment(LocalDateTime createPayment) {
        this.createPayment = createPayment;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }
}
