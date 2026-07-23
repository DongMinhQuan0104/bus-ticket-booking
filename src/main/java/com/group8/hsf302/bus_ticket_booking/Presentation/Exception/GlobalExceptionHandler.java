package com.group8.hsf302.bus_ticket_booking.Presentation.Exception;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.LoginForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.RegisterForm;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.EmailAlreadyExistsException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.InvalidCredentialsException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.BookingNotFoundException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.CannotCancelBookingException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.CannotReviewException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.PasswordConfirmNotMatchException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.SeatAlreadyBookedException;
import com.group8.hsf302.bus_ticket_booking.Domain.Exception.TripNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
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

    @ExceptionHandler(TripNotFoundException.class)
    public String handleTripNotFound(TripNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/trips/search";
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
