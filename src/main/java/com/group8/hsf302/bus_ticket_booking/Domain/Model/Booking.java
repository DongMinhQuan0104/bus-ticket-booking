package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingStatus;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "`booking`")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 30)
    private String bookingCode;

    @Column(nullable = false)
    private LocalDateTime dateBooked;

    @Min(value = 0, message = "total price can not negative")
    @Column(nullable = false)
    private Double totalPrice;

    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingType bookingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @NotBlank(message = "contact name can not blank")
    @Column(nullable = false)
    private String contactName;

    @NotBlank(message = "contact phone can not blank")
    @Column(nullable = false)
    private String contactPhone;

    @Email(message = "contact email not in the right format")
    private String contactEmail;

    private LocalDateTime expiresAt;

    // Customer sở hữu Booking. Có thể null với khách vãng lai.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    // Staff tạo Booking.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private Account createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BookingDetail> details = new ArrayList<>();

    public Booking() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public LocalDateTime getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(LocalDateTime dateBooked) {
        this.dateBooked = dateBooked;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BookingType getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = bookingType;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public List<BookingDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BookingDetail> details) {
        this.details = details;
    }

    public void addDetail(BookingDetail detail) {
        details.add(detail);
        detail.setBooking(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingCode='" + bookingCode + '\'' +
                ", dateBooked=" + dateBooked +
                ", totalPrice=" + totalPrice +
                ", bookingType=" + bookingType +
                ", status=" + status +
                '}';
    }
}