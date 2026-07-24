package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.*;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.*;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Staff.StaffService;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.BookingType;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.PaymentMethod;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Role;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Status;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.StaffBusinessException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(HttpSession session, Model model) {
        addCurrentStaff(session, model);
        return "staff/dashboard";
    }

    @GetMapping("/trips")
    public String trips(@RequestParam(required = false) String destinationFrom,
                        @RequestParam(required = false) String destinationTo,
                        @RequestParam(required = false) LocalDate departureDate,
                        HttpSession session,
                        Model model) {
        addCurrentStaff(session, model);
        model.addAttribute("destinationFrom", destinationFrom);
        model.addAttribute("destinationTo", destinationTo);
        model.addAttribute("departureDate", departureDate);

        boolean hasAny = hasText(destinationFrom) || hasText(destinationTo) || departureDate != null;
        boolean complete = hasText(destinationFrom) && hasText(destinationTo) && departureDate != null;

        if (hasAny && !complete) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ điểm đi, điểm đến và ngày khởi hành.");
        } else if (complete) {
            StaffTripSearchForm form = new StaffTripSearchForm(
                    destinationFrom.trim(), destinationTo.trim(), departureDate
            );
            model.addAttribute("trips", staffService.searchTrips(form));
            model.addAttribute("searched", true);
        }
        return "staff/trips";
    }

    @GetMapping("/trips/{tripId}/seats")
    public String seats(
            @PathVariable UUID tripId,
            @RequestParam(required = false) Integer pickupOrder,
            @RequestParam(required = false) Integer dropoffOrder,
            HttpSession session,
            Model model
    ) {
        addCurrentStaff(session, model);

        StaffTripViewModel trip =
                staffService.getTrip(tripId);

        List<StaffRouteStationViewModel> routeStations =
                staffService.getRouteStations(tripId);

        int effectivePickupOrder =
                pickupOrder != null
                        ? pickupOrder
                        : routeStations.get(0).stationOrder();

        int effectiveDropoffOrder =
                dropoffOrder != null
                        ? dropoffOrder
                        : routeStations
                          .get(routeStations.size() - 1)
                          .stationOrder();

        model.addAttribute("tripId", tripId);
        model.addAttribute("trip", trip);
        model.addAttribute("routeStations", routeStations);

        model.addAttribute(
                "pickupOrder",
                effectivePickupOrder
        );

        model.addAttribute(
                "dropoffOrder",
                effectiveDropoffOrder
        );

        model.addAttribute(
                "seats",
                staffService.getSeats(
                        tripId,
                        effectivePickupOrder,
                        effectiveDropoffOrder
                )
        );

        model.addAttribute("loaded", true);

        return "staff/seats";
    }

    @GetMapping("/bookings/new")
    public String newBooking(@RequestParam UUID tripId,
                             @RequestParam Integer pickupOrder,
                             @RequestParam Integer dropoffOrder,
                             @RequestParam(name = "selectedSeats", required = false) List<String> selectedSeats,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        addCurrentStaff(session, model);
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn ít nhất một ghế.");
            return "redirect:/staff/trips/" + tripId + "/seats?pickupOrder=" + pickupOrder + "&dropoffOrder=" + dropoffOrder;
        }
        model.addAttribute("tripId", tripId);
        model.addAttribute("pickupOrder", pickupOrder);
        model.addAttribute("dropoffOrder", dropoffOrder);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("bookingTypes", BookingType.values());
        return "staff/passenger-info";
    }

    @PostMapping("/bookings/create")
    public String createBooking(@RequestParam UUID tripId,
                                @RequestParam Integer pickupStationOrder,
                                @RequestParam Integer dropoffStationOrder,
                                @RequestParam String contactName,
                                @RequestParam String contactPhone,
                                @RequestParam(required = false) String contactEmail,
                                @RequestParam(required = false) String note,
                                @RequestParam BookingType bookingType,
                                @RequestParam List<String> passengerName,
                                @RequestParam List<String> seatCode,
                                @RequestParam(required = false) List<String> luggageWeightKg,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        UUID staffId = requireStaff(session).id();
        if (passengerName.size() != seatCode.size()) {
            throw new StaffBusinessException("Số hành khách phải khớp với số ghế đã chọn.");
        }

        List<StaffPassengerForm> passengers = new ArrayList<>();
        for (int i = 0; i < seatCode.size(); i++) {
            String luggageText = luggageWeightKg != null && i < luggageWeightKg.size()
                    ? luggageWeightKg.get(i) : null;
            Double luggage = hasText(luggageText) ? Double.valueOf(luggageText) : 0D;
            passengers.add(new StaffPassengerForm(passengerName.get(i), seatCode.get(i), luggage));
        }

        StaffCreateBookingForm form = new StaffCreateBookingForm(
                tripId, pickupStationOrder, dropoffStationOrder,
                contactName, contactPhone, contactEmail, note, bookingType, passengers
        );
        StaffBookingViewModel booking = staffService.createCounterBooking(form, staffId);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo booking thành công. Vui lòng xác nhận thanh toán.");
        return "redirect:/staff/bookings/" + booking.bookingId() + "/payment";
    }

    @GetMapping("/bookings/{bookingId}/payment")
    public String paymentPage(@PathVariable UUID bookingId,
                              HttpSession session,
                              Model model) {
        addCurrentStaff(session, model);
        model.addAttribute("booking", staffService.getBooking(bookingId));
        return "staff/payment";
    }

    @PostMapping("/bookings/{bookingId}/payment")
    public String confirmPayment(@PathVariable UUID bookingId,
                                 @RequestParam PaymentMethod paymentMethod,
                                 @RequestParam(required = false) String referenceCode,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        UUID staffId = requireStaff(session).id();
        StaffConfirmPaymentForm form = new StaffConfirmPaymentForm(paymentMethod, referenceCode);
        staffService.confirmPayment(bookingId, form, staffId);
        redirectAttributes.addFlashAttribute("successMessage", "Thanh toán thành công. Vé đã được phát hành.");
        return "redirect:/staff/bookings/" + bookingId + "/ticket";
    }

    @GetMapping("/bookings/{bookingId}/ticket")
    public String ticketSuccess(@PathVariable UUID bookingId,
                                HttpSession session,
                                Model model) {
        addCurrentStaff(session, model);
        model.addAttribute("booking", staffService.getBooking(bookingId));
        return "staff/ticket-success";
    }

    @GetMapping("/bookings")
    public String bookings(@RequestParam(required = false) String keyword,
                           HttpSession session,
                           Model model) {
        addCurrentStaff(session, model);
        model.addAttribute("keyword", keyword);
        if (hasText(keyword)) {
            model.addAttribute("bookings", staffService.searchBookings(keyword.trim()));
            model.addAttribute("searched", true);
        }
        return "staff/bookings";
    }

    @GetMapping("/bookings/{bookingId}")
    public String bookingDetail(@PathVariable UUID bookingId,
                                HttpSession session,
                                Model model) {
        addCurrentStaff(session, model);
        model.addAttribute("booking", staffService.getBooking(bookingId));
        return "staff/booking-detail";
    }

    @PostMapping("/bookings/{bookingId}/change-seat")
    public String changeSeat(@PathVariable UUID bookingId,
                             @RequestParam UUID bookingDetailId,
                             @RequestParam String newSeatCode,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        UUID staffId = requireStaff(session).id();
        staffService.changeSeat(bookingId, new StaffChangeSeatForm(bookingDetailId, newSeatCode), staffId);
        redirectAttributes.addFlashAttribute("successMessage", "Đổi ghế thành công.");
        return "redirect:/staff/bookings/" + bookingId;
    }

    @PostMapping("/bookings/{bookingId}/cancel")
    public String cancelBooking(@PathVariable UUID bookingId,
                                @RequestParam String reason,
                                @RequestParam(defaultValue = "false") boolean requestRefund,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        UUID staffId = requireStaff(session).id();
        staffService.cancelBooking(bookingId, new StaffCancelBookingForm(reason, requestRefund), staffId);
        redirectAttributes.addFlashAttribute("successMessage", "Hủy booking thành công.");
        return "redirect:/staff/bookings/" + bookingId;
    }

    @PostMapping("/bookings/{bookingId}/refund")
    public String refund(@PathVariable UUID bookingId,
                         @RequestParam String reason,
                         @RequestParam(defaultValue = "false") boolean escalateToAdmin,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        UUID staffId = requireStaff(session).id();
        staffService.processRefund(bookingId, new StaffRefundForm(reason, escalateToAdmin), staffId);
        redirectAttributes.addFlashAttribute("successMessage",
                escalateToAdmin ? "Yêu cầu đã được chuyển cho Admin." : "Hoàn tiền thành công.");
        return "redirect:/staff/bookings/" + bookingId;
    }

    @GetMapping("/check-in")
    public String checkInPage(@RequestParam(required = false) UUID tripId,
                              HttpSession session,
                              Model model) {
        addCurrentStaff(session, model);
        model.addAttribute("tripId", tripId);
        return "staff/check-in";
    }

    @PostMapping("/check-in")
    public String checkIn(@RequestParam String ticketCode,
                          @RequestParam UUID tripId,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        UUID staffId = requireStaff(session).id();
        StaffCheckInViewModel result = staffService.checkIn(new StaffCheckInForm(ticketCode, tripId), staffId);
        redirectAttributes.addFlashAttribute("checkInResult", result);
        redirectAttributes.addFlashAttribute("successMessage", "Check-in thành công.");
        return "redirect:/staff/check-in?tripId=" + tripId;
    }

    @GetMapping("/support-requests")
    public String supportPage(@RequestParam(required = false) String bookingKeyword,
                              @RequestParam(required = false) UUID bookingId,
                              HttpSession session,
                              Model model) {
        addCurrentStaff(session, model);
        model.addAttribute("bookingKeyword", bookingKeyword);
        if (hasText(bookingKeyword)) {
            model.addAttribute("bookingResults", staffService.searchBookings(bookingKeyword.trim()));
        }
        if (bookingId != null) {
            model.addAttribute("selectedBooking", staffService.getBooking(bookingId));
        }
        return "staff/support-requests";
    }

    @PostMapping("/support-requests/create")
    public String createSupportRequest(@RequestParam(required = false) UUID bookingId,
                                       @RequestParam String subject,
                                       @RequestParam String description,
                                       @RequestParam(defaultValue = "false") boolean escalateToAdmin,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        UUID staffId = requireStaff(session).id();
        StaffSupportRequestViewModel result = staffService.createSupportRequest(
                new StaffSupportRequestForm(bookingId, subject, description, escalateToAdmin), staffId
        );
        redirectAttributes.addFlashAttribute("supportResult", result);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo yêu cầu hỗ trợ thành công.");
        return "redirect:/staff/support-requests";
    }

    private void addCurrentStaff(HttpSession session, Model model) {
        model.addAttribute("currentUser", requireStaff(session));
    }

    private AccountViewModel requireStaff(HttpSession session) {
        Object value = session.getAttribute("LOGGED_IN_USER");
        if (!(value instanceof AccountViewModel currentUser)) {
            throw new StaffBusinessException("Vui lòng đăng nhập trước khi sử dụng chức năng Staff.");
        }
        if (currentUser.role() != Role.STAFF) {
            throw new StaffBusinessException("Tài khoản cần có quyền STAFF.");
        }
        if (currentUser.status() != Status.AVAILABLE) {
            throw new StaffBusinessException("Tài khoản Staff hiện không khả dụng.");
        }
        if (currentUser.id() == null) {
            throw new StaffBusinessException("Không tìm thấy mã tài khoản Staff.");
        }
        return currentUser;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
