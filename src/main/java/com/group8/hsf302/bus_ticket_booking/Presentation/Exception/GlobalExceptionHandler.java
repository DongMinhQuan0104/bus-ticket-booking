package com.group8.hsf302.bus_ticket_booking.Presentation.Exception;

import com.group8.hsf302.bus_ticket_booking.Domain.Exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExist(
            EmailAlreadyExistsException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/auth/register");
    }

    @ExceptionHandler(PasswordConfirmNotMatchException.class)
    public String handlePasswordNotMatch(
            PasswordConfirmNotMatchException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/auth/register");
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(
            InvalidCredentialsException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/auth/login");
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFound(
            AccountNotFoundException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(BusNotFoundException.class)
    public String handleBusNotFound(
            BusNotFoundException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    @ExceptionHandler(LicensePlateAlreadyExistsException.class)
    public String handleLicensePlateAlreadyExists(
            LicensePlateAlreadyExistsException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    @ExceptionHandler(OldPasswordNotMatchException.class)
    public String handleOldPasswordNotMatch(
            OldPasswordNotMatchException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(RouteAlreadyExistsException.class)
    public String handleRouteAlreadyExists(
            RouteAlreadyExistsException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    @ExceptionHandler(RouteStationNotFound.class)
    public String handleRouteNotFound(
            RouteStationNotFound e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    @ExceptionHandler(StationAlreadyExistsException.class)
    public String handleStationAlreadyExists(
            StationAlreadyExistsException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    @ExceptionHandler(StationNotFoundException.class)
    public String handleStationNotFound(
            StationNotFoundException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    @ExceptionHandler(TripNotFoundException.class)
    public String handleTripNotFound(
            TripNotFoundException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    @ExceptionHandler(StaffBusinessException.class)
    public String handleStaffBusinessException(
            StaffBusinessException exception,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                exception.getMessage()
        );

        String message = exception.getMessage();

        if (message != null && (
                message.contains("đăng nhập")
                        || message.contains("quyền STAFF")
                        || message.contains("Staff hiện không khả dụng")
                        || message.contains("mã tài khoản Staff")
        )) {
            return "redirect:/auth/login";
        }

        String referer = request.getHeader("Referer");

        if (referer != null && referer.contains("/staff")) {
            return "redirect:" + referer;
        }

        return "redirect:/staff/dashboard";
    }
}