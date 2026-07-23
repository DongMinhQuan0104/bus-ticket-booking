package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.ChangePasswordForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.UpdateAccountForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Customer.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quan ly HO SO CA NHAN cua khach hang (tang Presentation).
 * Noi cac method da co san trong CustomerService (truoc do chua co endpoint):
 * <ul>
 *   <li>GET  /profile                  - xem ho so (getAccount)</li>
 *   <li>POST /profile                  - cap nhat thong tin (update)</li>
 *   <li>POST /profile/change-password  - doi mat khau (changePassword)</li>
 *   <li>POST /profile/deactivate       - vo hieu hoa tai khoan (deleted - xoa mem)</li>
 * </ul>
 * Tat ca yeu cau dang nhap; thao tac tren dung tai khoan trong session.
 */
@Controller
@RequestMapping("/profile")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /** Xem ho so ca nhan. */
    @GetMapping
    public String profile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        AccountViewModel account = customerService.getAccount(currentUser.id());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("account", account);
        if (!model.containsAttribute("updateForm")) {
            model.addAttribute("updateForm", new UpdateAccountForm());
        }
        if (!model.containsAttribute("changePasswordForm")) {
            model.addAttribute("changePasswordForm", new ChangePasswordForm());
        }
        return "customer/profile";
    }

    /** Cap nhat thong tin ca nhan; lam moi lai session sau khi doi. */
    @PostMapping
    public String update(@Valid @ModelAttribute("updateForm") UpdateAccountForm form,
                         BindingResult bindingResult,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thông tin cập nhật không hợp lệ");
            return "redirect:/profile";
        }
        AccountViewModel updated = customerService.update(form, currentUser.id());
        session.setAttribute("LOGGED_IN_USER", updated); // dong bo lai ten/email tren header
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công");
        return "redirect:/profile";
    }

    /** Doi mat khau (loi nghiep vu do GlobalExceptionHandler xu ly). */
    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("changePasswordForm") ChangePasswordForm form,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        customerService.changePassword(form, currentUser.id());
        redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công");
        return "redirect:/profile";
    }

    /** Vo hieu hoa (xoa mem) tai khoan roi dang xuat. */
    @PostMapping("/deactivate")
    public String deactivate(HttpSession session, RedirectAttributes redirectAttributes) {
        AccountViewModel currentUser = (AccountViewModel) session.getAttribute("LOGGED_IN_USER");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        customerService.deleted(currentUser.id());
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Tài khoản đã được vô hiệu hóa");
        return "redirect:/home";
    }
}
