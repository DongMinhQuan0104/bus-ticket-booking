package com.group8.hsf302.bus_ticket_booking.Presentation.Exception;

import com.group8.hsf302.bus_ticket_booking.Domain.Exception.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExist(EmailAlreadyExistsException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/auth/register");
    }

    @ExceptionHandler(PasswordConfirmNotMatchException.class)
    public String handlePasswordNotMatch(PasswordConfirmNotMatchException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/auth/register");
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(InvalidCredentialsException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/auth/login");
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFound(AccountNotFoundException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : ".....");
    }

    @ExceptionHandler(BusNotFoundException.class)
    public String handleBusNotFound(BusNotFoundException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : ".....");
    }

    @ExceptionHandler(LicensePlateAlreadyExistsException.class)
    public String handleLicensePlateAlreadyExists(LicensePlateAlreadyExistsException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : ".....");
    }

    @ExceptionHandler(OldPasswordNotMatchException.class)
    public String handleOldPasswordNotMatch(OldPasswordNotMatchException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : ".....");
    }

    @ExceptionHandler(RouteAlreadyExistsException.class)
    public String handleRouteAlreadyExists(RouteAlreadyExistsException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : ".....");
    }

    @ExceptionHandler(RouteStationNotFound.class)
    public String handleRouteNotFound(RouteStationNotFound e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : ".....");
    }

    @ExceptionHandler(StationAlreadyExistsException.class)
    public String handleStationAlreadyExists(StationAlreadyExistsException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : ".....");
    }

    @ExceptionHandler(StationNotFoundException.class)
    public String handleStationNotFound(StationNotFoundException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : ".....");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/home";
    }

    @ExceptionHandler(TripNotFoundException.class)
    public String handleTripNotFound(TripNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/driver/trips";
    }

    @ExceptionHandler(BookingDetailNotFoundException.class)
    public String handleBookingDetailNotFound(BookingDetailNotFoundException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/driver/trips");
    }

    @ExceptionHandler(AlreadyCheckedInException.class)
    public String handleAlreadyCheckedIn(AlreadyCheckedInException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/driver/trips");
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/driver/trips");
    }
}
