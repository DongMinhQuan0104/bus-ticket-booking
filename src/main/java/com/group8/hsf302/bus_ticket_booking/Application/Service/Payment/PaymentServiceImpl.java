package com.group8.hsf302.bus_ticket_booking.Application.Service.Payment;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.PaymentRequest;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.PaymentResponse;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Booking;
import com.group8.hsf302.bus_ticket_booking.Domain.Model.Payment;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.BookingRepo;
import com.group8.hsf302.bus_ticket_booking.Domain.Repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        Booking booking = bookingRepo.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = new Payment();
        payment.setCreatePayment(LocalDateTime.now());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setBooking(booking);

        Payment savedPayment = paymentRepo.save(payment);

        return new PaymentResponse(
                savedPayment.getId(),
                savedPayment.getCreatePayment(),
                savedPayment.getPaymentMethod(),
                savedPayment.getBooking().getId()
        );
    }
}
