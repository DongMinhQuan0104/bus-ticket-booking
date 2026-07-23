package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.BookingViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Customer.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class MyTicketController {

    private final CustomerService customerService;

    public MyTicketController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // E4 - Ve cua toi
    @GetMapping("/my-tickets")
    public String myTickets(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để xem vé");
            return "redirect:/auth/login";
        }
        List<BookingViewModel> bookings = customerService.getMyBookings(currentUser.id());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("bookings", bookings);
        return "my-tickets";
    }

    // E5 - Trang xac nhan huy ve (kem chinh sach hoan tien)
    @GetMapping("/my-tickets/{id}/cancel")
    public String cancelForm(@PathVariable UUID id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        BookingViewModel booking = customerService.getMyBooking(id, currentUser.id());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("booking", booking);

        double total = booking.totalPrice() != null ? booking.totalPrice() : 0.0;
        long hoursLeft = booking.departureTime() != null
                ? Duration.between(LocalDateTime.now(), booking.departureTime()).toHours() : 0;
        int refundPercent = hoursLeft >= 24 ? 90 : (hoursLeft >= 12 ? 50 : 0);
        model.addAttribute("hoursLeft", hoursLeft);
        model.addAttribute("refundPercent", refundPercent);
        model.addAttribute("refundAmount", total * refundPercent / 100.0);
        return "booking-cancel";
    }

    // E5 - Thuc hien huy ve
    @PostMapping("/my-tickets/{id}/cancel")
    public String doCancel(@PathVariable UUID id, HttpSession session, RedirectAttributes redirectAttributes) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        customerService.cancelBooking(id, currentUser.id());
        redirectAttributes.addFlashAttribute("successMessage", "Đã hủy vé thành công. Ghế đã được giải phóng.");
        return "redirect:/my-tickets";
    }
}
