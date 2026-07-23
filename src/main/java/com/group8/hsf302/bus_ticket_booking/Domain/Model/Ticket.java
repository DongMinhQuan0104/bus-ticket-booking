package com.group8.hsf302.bus_ticket_booking.Domain.Model;

import com.group8.hsf302.bus_ticket_booking.Domain.Enum.TicketStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            nullable = false,
            unique = true,
            length = 40
    )
    private String ticketCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime checkedInAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_in_by_id")
    private Account checkedInBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "booking_detail_id",
            nullable = false,
            unique = true
    )
    private BookingDetail bookingDetail;

    public Ticket() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public Account getCheckedInBy() {
        return checkedInBy;
    }

    public void setCheckedInBy(Account checkedInBy) {
        this.checkedInBy = checkedInBy;
    }

    public BookingDetail getBookingDetail() {
        return bookingDetail;
    }

    public void setBookingDetail(
            BookingDetail bookingDetail
    ) {
        this.bookingDetail = bookingDetail;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null
                || getClass() != object.getClass()) {
            return false;
        }

        Ticket ticket = (Ticket) object;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", ticketCode='" + ticketCode + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", checkedInAt=" + checkedInAt +
                '}';
    }
}