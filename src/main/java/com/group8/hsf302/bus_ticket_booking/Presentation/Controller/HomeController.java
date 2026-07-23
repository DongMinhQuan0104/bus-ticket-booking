package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Customer.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CustomerService customerService;

    public HomeController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /** Vao thang "/" -> ve trang chu (trang mac dinh khi chay ung dung). */
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        model.addAttribute("currentUser", currentUser);
        // Goi y thanh pho co chuyen -> FE hien datalist, khach khoi go tay
        model.addAttribute("departureCities", customerService.getDepartureCities());
        model.addAttribute("arrivalCities", customerService.getArrivalCities());
        return "auth/home";
    }
}