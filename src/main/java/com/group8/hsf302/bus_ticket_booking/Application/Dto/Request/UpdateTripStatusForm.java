package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import jakarta.validation.constraints.NotNull;

public class UpdateTripStatusForm {

    @NotNull(message = "Status cannot be null")
    private Status status;

    public UpdateTripStatusForm() {
    }

    public UpdateTripStatusForm(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
