package com.group8.hsf302.bus_ticket_booking.Presentation.Exception;

import com.group8.hsf302.bus_ticket_booking.Domain.Exception.*;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ===== Authentication / tai khoan (co san) =====

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

    @ExceptionHandler(OldPasswordNotMatchException.class)
    public String handleOldPasswordNotMatch(OldPasswordNotMatchException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : ".....");
    }

    // ===== Admin: Bus / Route / Station (nhanh main - Quan) =====

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

    // ===== Driver (An) =====

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/home";
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

    // ===== Customer: dat ve / huy / danh gia (E1-E6 - Viet) =====
    // TripNotFound dung chung cho ca Customer va Driver: dua vao Referer de quay ve dung trang
    // (khach -> /trips/search, tai xe -> /driver/trips). Gop 2 handler trung thanh 1 de tranh loi khoi dong Spring.

    @ExceptionHandler(TripNotFoundException.class)
    public String handleTripNotFound(TripNotFoundException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/trips/search");
    }

    @ExceptionHandler(SeatAlreadyBookedException.class)
    public String handleSeatAlreadyBooked(SeatAlreadyBookedException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Một số ghế vừa được người khác đặt. Vui lòng chọn lại.");
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/trips/search");
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public String handleBookingNotFound(BookingNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy vé");
        return "redirect:/my-tickets";
    }

    @ExceptionHandler(CannotCancelBookingException.class)
    public String handleCannotCancel(CannotCancelBookingException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Vé không thể hủy (chuyến đã khởi hành).");
        return "redirect:/my-tickets";
    }

    @ExceptionHandler(CannotReviewException.class)
    public String handleCannotReview(CannotReviewException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/my-tickets";
    }
}
