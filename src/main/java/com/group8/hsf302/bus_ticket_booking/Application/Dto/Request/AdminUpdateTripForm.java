package com.group8.hsf302.bus_ticket_booking.Application.Dto.Request;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Form cap nhat chuyen (Admin - B4). Truoc day rong nen BE khong sua duoc chuyen.
 * Khong dat @NotBlank de cho phep cap nhat tung phan (updateEntityFromForm bo qua field null).
 */
public class AdminUpdateTripForm {

    private String destinationFrom;
    private String destinationTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureTime;

    private String driverName;
    private Double price;
    private UUID routeId;
    private UUID busId;
    private TripStatus status;

    public AdminUpdateTripForm() {
    }

    public String getDestinationFrom() { return destinationFrom; }
    public void setDestinationFrom(String destinationFrom) { this.destinationFrom = destinationFrom; }

    public String getDestinationTo() { return destinationTo; }
    public void setDestinationTo(String destinationTo) { this.destinationTo = destinationTo; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public UUID getRouteId() { return routeId; }
    public void setRouteId(UUID routeId) { this.routeId = routeId; }

    public UUID getBusId() { return busId; }
    public void setBusId(UUID busId) { this.busId = busId; }

    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }
}
