package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateTripStatusForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.DriverTripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.PassengerManifestViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Driver.DriverService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/trips")
    public String showAssignedTrips(HttpSession session, Model model) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        List<DriverTripViewModel> trips = driverService.getAssignedTrips(currentUser.getFullName());
        model.addAttribute("trips", trips);
        model.addAttribute("currentUser", currentUser);
        return "driver/trips";
    }

    @GetMapping("/trips/{id}/manifest")
    public String showPassengerManifest(@PathVariable("id") UUID tripId, HttpSession session, Model model) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        DriverTripViewModel trip = driverService.getTripById(tripId);
        List<PassengerManifestViewModel> manifest = driverService.getPassengerManifest(tripId);
        model.addAttribute("trip", trip);
        model.addAttribute("manifest", manifest);
        model.addAttribute("currentUser", currentUser);
        return "driver/manifest";
    }

    @PostMapping("/trips/{id}/status")
    public String updateTripStatus(@PathVariable("id") UUID tripId,
                                  @Valid @ModelAttribute("statusForm") UpdateTripStatusForm form,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid status form data");
            return "redirect:/driver/trips";
        }
        driverService.updateTripStatus(tripId, form.getStatus());
        redirectAttributes.addFlashAttribute("successMessage", "Trip status updated successfully");
        return "redirect:/driver/trips";
    }

    @PostMapping("/tickets/{id}/checkin")
    public String checkInPassenger(@PathVariable("id") UUID bookingDetailId,
                                   @RequestParam("tripId") UUID tripId,
                                   RedirectAttributes redirectAttributes) {
        driverService.checkInPassenger(bookingDetailId);
        redirectAttributes.addFlashAttribute("successMessage", "Passenger checked in successfully");
        return "redirect:/driver/trips/" + tripId + "/manifest";
    }
}
