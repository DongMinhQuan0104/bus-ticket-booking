package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Customer.CustomerSearchTripService;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SameStationException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/trips")
public class CustomerTripController {

    private final CustomerSearchTripService searchTripService;

    public CustomerTripController(CustomerSearchTripService searchTripService) {
        this.searchTripService = searchTripService;
    }

    @GetMapping("/search")
    public String search(@Valid @ModelAttribute("searchForm") SearchTripForm searchForm,
                         BindingResult bindingResult,
                         @RequestParam(value = "submitted", required = false) String submitted,
                         HttpSession session,
                         Model model) {

        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("today", LocalDate.now());

        boolean isSubmitted = submitted != null;
        model.addAttribute("submitted", isSubmitted);

        // Lan dau vao trang (chua bam tim): chi hien form, khong validate
        if (!isSubmitted) {
            return "trip-search";
        }

        model.addAttribute("showResults", false);
        if (!bindingResult.hasErrors()) {
            try {
                List<TripViewModel> trips = searchTripService.search(searchForm);
                model.addAttribute("trips", trips);
                model.addAttribute("showResults", true);
            } catch (SameStationException e) {
                bindingResult.rejectValue("destinationTo", "sameStation", e.getMessage());
            }
        }

        return "trip-search";
    }
}
