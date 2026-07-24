package com.group8.hsf302.bus_ticket_booking.Application.Dto.Response;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.SupportRequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record StaffSupportRequestViewModel(
        UUID id,
        String subject,
        String description,
        SupportRequestStatus status,
        LocalDateTime createdAt
) {
}