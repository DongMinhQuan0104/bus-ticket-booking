package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.*;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Staff.StaffService;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Role;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.StaffBusinessException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(
            StaffService staffService
    ) {
        this.staffService = staffService;
    }

    @PostMapping("/trips/search")
    public ResponseEntity<List<StaffTripViewModel>>
    searchTrips(
            @Valid
            @RequestBody
            StaffTripSearchForm form,
            HttpSession session
    ) {
        requireStaffId(session);

        return ResponseEntity.ok(
                staffService.searchTrips(form)
        );
    }

    @GetMapping("/trips/{tripId}/seats")
    public ResponseEntity<List<StaffSeatViewModel>>
    getSeats(
            @PathVariable UUID tripId,
            @RequestParam Integer pickupOrder,
            @RequestParam Integer dropoffOrder,
            HttpSession session
    ) {
        requireStaffId(session);

        return ResponseEntity.ok(
                staffService.getSeats(
                        tripId,
                        pickupOrder,
                        dropoffOrder
                )
        );
    }

    @PostMapping("/bookings")
    public ResponseEntity<StaffBookingViewModel>
    createCounterBooking(
            @Valid
            @RequestBody
            StaffCreateBookingForm form,
            HttpSession session
    ) {
        UUID staffId = requireStaffId(session);

        StaffBookingViewModel result =
                staffService.createCounterBooking(
                        form,
                        staffId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @PostMapping(
            "/bookings/{bookingId}/payment"
    )
    public ResponseEntity<StaffBookingViewModel>
    confirmPayment(
            @PathVariable UUID bookingId,
            @Valid
            @RequestBody
            StaffConfirmPaymentForm form,
            HttpSession session
    ) {
        UUID staffId = requireStaffId(session);

        return ResponseEntity.ok(
                staffService.confirmPayment(
                        bookingId,
                        form,
                        staffId
                )
        );
    }

    @GetMapping("/bookings")
    public ResponseEntity
            <List<StaffBookingViewModel>>
    searchBookings(
            @RequestParam String keyword,
            HttpSession session
    ) {
        requireStaffId(session);

        return ResponseEntity.ok(
                staffService.searchBookings(
                        keyword
                )
        );
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<StaffBookingViewModel>
    getBooking(
            @PathVariable UUID bookingId,
            HttpSession session
    ) {
        requireStaffId(session);

        return ResponseEntity.ok(
                staffService.getBooking(
                        bookingId
                )
        );
    }

    @PutMapping(
            "/bookings/{bookingId}/seat"
    )
    public ResponseEntity<StaffBookingViewModel>
    changeSeat(
            @PathVariable UUID bookingId,
            @Valid
            @RequestBody
            StaffChangeSeatForm form,
            HttpSession session
    ) {
        UUID staffId = requireStaffId(session);

        return ResponseEntity.ok(
                staffService.changeSeat(
                        bookingId,
                        form,
                        staffId
                )
        );
    }

    @PutMapping(
            "/bookings/{bookingId}/cancel"
    )
    public ResponseEntity<StaffBookingViewModel>
    cancelBooking(
            @PathVariable UUID bookingId,
            @Valid
            @RequestBody
            StaffCancelBookingForm form,
            HttpSession session
    ) {
        UUID staffId = requireStaffId(session);

        return ResponseEntity.ok(
                staffService.cancelBooking(
                        bookingId,
                        form,
                        staffId
                )
        );
    }

    @PutMapping(
            "/bookings/{bookingId}/refund"
    )
    public ResponseEntity<StaffBookingViewModel>
    processRefund(
            @PathVariable UUID bookingId,
            @Valid
            @RequestBody
            StaffRefundForm form,
            HttpSession session
    ) {
        UUID staffId = requireStaffId(session);

        return ResponseEntity.ok(
                staffService.processRefund(
                        bookingId,
                        form,
                        staffId
                )
        );
    }

    @PostMapping("/check-in")
    public ResponseEntity<StaffCheckInViewModel>
    checkIn(
            @Valid
            @RequestBody
            StaffCheckInForm form,
            HttpSession session
    ) {
        UUID staffId = requireStaffId(session);

        return ResponseEntity.ok(
                staffService.checkIn(
                        form,
                        staffId
                )
        );
    }

    @PostMapping("/support-requests")
    public ResponseEntity
            <StaffSupportRequestViewModel>
    createSupportRequest(
            @Valid
            @RequestBody
            StaffSupportRequestForm form,
            HttpSession session
    ) {
        UUID staffId = requireStaffId(session);

        StaffSupportRequestViewModel result =
                staffService
                        .createSupportRequest(
                                form,
                                staffId
                        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @PostMapping("/holds/release-expired")
    public ResponseEntity<Integer>
    releaseExpiredHolds(
            HttpSession session
    ) {
        requireStaffId(session);

        return ResponseEntity.ok(
                staffService.releaseExpiredHolds()
        );
    }

    private UUID requireStaffId(
            HttpSession session
    ) {
        Object sessionValue =
                session.getAttribute(
                        "LOGGED_IN_USER"
                );

        if (!(sessionValue
                instanceof AccountViewModel currentUser)) {
            throw new StaffBusinessException(
                    "Please log in before using Staff functions"
            );
        }

        if (currentUser.role() != Role.STAFF) {
            throw new StaffBusinessException(
                    "Only STAFF account can access this function"
            );
        }

        if (currentUser.status()
                != Status.AVAILABLE) {
            throw new StaffBusinessException(
                    "Staff account is not available"
            );
        }

        if (currentUser.id() == null) {
            throw new StaffBusinessException(
                    "Staff account ID is missing"
            );
        }

        return currentUser.id();
    }
}