package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public class CreateBookingForm {
    private LocalDateTime dateBooked;

    @Min(value = 0,message = "total price can not negative")
    private Double totalPrice;

    private String note;

    @Enumerated(EnumType.STRING)
    private BookingType bookingType;

}
