package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.CreateTripForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.TripViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Trip.TripService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/admin/trips/create")
    @ResponseBody
    public ResponseEntity<?> createTrip(@Valid @ModelAttribute CreateTripForm form,
                                        BindingResult bindingResult) {

        System.out.println("===== CREATE TRIP FORM DATA =====");
        System.out.println("From: " + form.getDestinationFrom());
        System.out.println("To: " + form.getDestinationTo());
        System.out.println("Departure: " + form.getDepartureTime());
        System.out.println("Arrival: " + form.getArrivalTime());
        System.out.println("Bus code: " + form.getBusCode());
        System.out.println("License plate: " + form.getBusLicensePlate());
        System.out.println("Vehicle type: " + form.getVehicleType());
        System.out.println("Driver name: " + form.getDriverName());
        System.out.println("Price: " + form.getPrice());

        if (bindingResult.hasErrors()) {
            System.out.println("===== CREATE TRIP VALIDATION ERRORS =====");
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("ERROR: " + error.getDefaultMessage());
            });

            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        TripViewModel trip = tripService.createTrip(form);

        System.out.println("===== CREATE TRIP SUCCESS =====");
        System.out.println("Trip id: " + trip.getId());

        return ResponseEntity.ok(trip);
    }

    @GetMapping("/admin/trips")
    @ResponseBody
    public ResponseEntity<List<TripViewModel>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    @GetMapping("/admin/trips/{tripId}")
    @ResponseBody
    public ResponseEntity<TripViewModel> getTripById(@PathVariable UUID tripId) {
        return ResponseEntity.ok(tripService.getTripById(tripId));
    }
}