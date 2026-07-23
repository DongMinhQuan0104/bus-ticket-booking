package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateTripStatusForm {

    @NotNull(message = "Status cannot be null")
    private TripStatus status;

    public UpdateTripStatusForm() {
    }

    public UpdateTripStatusForm(TripStatus status) {
        this.status = status;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }
}
