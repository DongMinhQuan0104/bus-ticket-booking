package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.SearchTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Customer.CustomerService;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SameStationException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/trips")
public class CustomerTripController {

    private final CustomerService customerService;

    public CustomerTripController(CustomerService customerService) {
        this.customerService = customerService;
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
                List<TripViewModel> trips = customerService.searchTrips(searchForm);
                model.addAttribute("trips", trips);
                model.addAttribute("showResults", true);
            } catch (SameStationException e) {
                bindingResult.rejectValue("destinationTo", "sameStation", e.getMessage());
            }
        }

        return "trip-search";
    }

    // E2 - Trang chon ghe & dat ve
    @GetMapping("/{tripId}/booking")
    public String bookingPage(@PathVariable UUID tripId, HttpSession session, Model model) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        model.addAttribute("currentUser", currentUser);

        TripViewModel trip = customerService.getTripForBooking(tripId);
        List<String> occupiedSeatCodes = customerService.getOccupiedSeatCodes(tripId);

        // Sinh danh sach ma ghe theo suc chua thuc te cua xe (16 hoac 32 cho)
        int seats = trip.busCapacity() != null ? trip.busCapacity().getSeats() : 0;
        List<String> seatCodes = new ArrayList<>();
        for (int i = 1; i <= seats; i++) {
            seatCodes.add(String.format("A%02d", i));
        }

        model.addAttribute("trip", trip);
        model.addAttribute("seatCodes", seatCodes);
        model.addAttribute("occupiedSeatCodes", occupiedSeatCodes);
        model.addAttribute("maxSeatsPerBooking", 4);
        return "trip-booking";
    }
}
