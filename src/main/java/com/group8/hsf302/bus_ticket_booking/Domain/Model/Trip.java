package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TripStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "destination can not blank")
    private String destinationFrom;

    @NotBlank(message = "destination can not blank")
    private String destinationTo;

    private LocalDateTime departureTime;

    @NotBlank(message = "driver name can not blank")
    private String driverName;

    // Gia ve co ban cua chuyen (them khi lam E3). Dung de tinh tong tien khi dat ve.
    @Min(value = 0, message = "price can not be negative")
    private Double price;

    @Enumerated(EnumType.STRING)
    private TripStatus status = TripStatus.SCHEDULED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus")
    private Bus bus;

    public Trip() {
    }

    public Trip(String destinationFrom, String destinationTo, LocalDateTime departureTime, String driverName, TripStatus status, Route route, Bus bus) {
        this.destinationFrom = destinationFrom;
        this.destinationTo = destinationTo;
        this.departureTime = departureTime;
        this.driverName = driverName;
        this.status = status;
        this.route = route;
        this.bus = bus;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    /**
     * Chuyen con dat ve duoc khong? (dung chung 1 truong trang thai TripStatus)
     * "Con dat duoc" = chuyen moi len lich (SCHEDULED). Cac trang thai READY/RUNNING/COMPLETED
     * la do tai xe dieu khien (nghiep vu Driver) va khong cho khach dat nua.
     * Thay cho enum Status.AVAILABLE cu ben Customer (E1/E2/E3).
     */
    public boolean isBookable() {
        return status == TripStatus.SCHEDULED;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", destinationFrom='" + destinationFrom + '\'' +
                ", destinationTo='" + destinationTo + '\'' +
                ", departureTime=" + departureTime +
                ", driverName='" + driverName + '\'' +
                ", status=" + status +
                ", route=" + route +
                ", bus=" + bus +
                '}';
    }
}
