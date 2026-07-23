package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.BookingViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Customer.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
}
