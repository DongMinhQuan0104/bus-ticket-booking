package com.group8.hsf302.bus_ticket_booking.Application.Service.Payment;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.PaymentRequest;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
}
