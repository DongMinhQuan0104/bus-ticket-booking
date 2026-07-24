package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * E1 - Form nguoi dung nhap de tim chuyen xe (Request DTO).
 * Validation chay o server (@Valid): diem di/den khong duoc rong, ngay di khong duoc trong qua khu.
 * Rieng "diem di != diem den" duoc kiem tra o tang service vi la rang buoc nghiep vu giua 2 truong.
 */
public class SearchTripForm {

    @NotBlank(message = "departure point can not blank")
    private String destinationFrom;

    @NotBlank(message = "destination point can not blank")
    private String destinationTo;

    // @FutureOrPresent: chan ngay qua khu ngay tai server (khong chi dua vao khoa date-picker o FE)
    @NotNull(message = "departure date can not blank")
    @FutureOrPresent(message = "departure date can not be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

    public SearchTripForm() {
    }

    public String getDestinationFrom() {
        return destinationFrom;
    }

    public void setDestinationFrom(String destinationFrom) {
        this.destinationFrom = destinationFrom;
    }

    public String getDestinationTo() {
        return destinationTo;
    }

    public void setDestinationTo(String destinationTo) {
        this.destinationTo = destinationTo;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}
