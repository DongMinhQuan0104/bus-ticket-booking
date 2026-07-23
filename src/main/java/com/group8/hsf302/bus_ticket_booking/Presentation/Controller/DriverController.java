package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateTripStatusForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.DriverTripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.PassengerManifestViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Driver.DriverService;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Role;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.AccessDeniedException;
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

    private AccountViewModel verifyDriverAuth(HttpSession session) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            throw new AccessDeniedException("Please login to access the Driver Portal.");
        }
        if (currentUser.role() != Role.DRIVER) {
            throw new AccessDeniedException("Access denied. DRIVER role is required.");
        }
        return currentUser;
    }

    @GetMapping("/trips")
    public String showAssignedTrips(HttpSession session, Model model) {
        AccountViewModel currentUser = verifyDriverAuth(session);
        List<DriverTripViewModel> trips = driverService.getAssignedTrips(currentUser.fullName());
        model.addAttribute("trips", trips);
        model.addAttribute("currentUser", currentUser);
        return "driver/trips";
    }

    @GetMapping({"/trips/{id}/manifest", "/trips/{id}/passengers"})
    public String showPassengerManifest(@PathVariable("id") UUID tripId, HttpSession session, Model model) {
        AccountViewModel currentUser = verifyDriverAuth(session);
        DriverTripViewModel trip = driverService.getTripById(tripId);
        List<PassengerManifestViewModel> manifest = driverService.getPassengerManifest(tripId);
        model.addAttribute("trip", trip);
        model.addAttribute("manifest", manifest);
        model.addAttribute("currentUser", currentUser);
        return "driver/passengers";
    }

    @PostMapping("/trips/{id}/status")
    public String updateTripStatus(@PathVariable("id") UUID tripId,
                                  @Valid @ModelAttribute("statusForm") UpdateTripStatusForm form,
                                  BindingResult bindingResult,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        verifyDriverAuth(session);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid status form data");
            return "redirect:/driver/trips";
        }
        driverService.updateTripStatus(tripId, form.getStatus());
        redirectAttributes.addFlashAttribute("successMessage", "Trip status updated successfully");
        return "redirect:/driver/trips";
    }

    @GetMapping("/profile")
    public String showDriverProfile(HttpSession session, Model model) {
        AccountViewModel currentUser = verifyDriverAuth(session);
        model.addAttribute("currentUser", currentUser);
        return "driver/profile";
    }

    @PostMapping("/tickets/{id}/checkin")
    public String checkInPassenger(@PathVariable("id") UUID bookingDetailId,
                                   @RequestParam("tripId") UUID tripId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        verifyDriverAuth(session);
        driverService.checkInPassenger(bookingDetailId);
        redirectAttributes.addFlashAttribute("successMessage", "Passenger checked in successfully");
        return "redirect:/driver/trips/" + tripId + "/passengers";
    }
}
