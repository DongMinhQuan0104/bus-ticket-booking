package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.LoginForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.RegisterForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Authentication.AuthenticationService;
import com.group8.hsf302.bus_ticket_booking.Domain.Enum.Role;
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

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if(!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if(!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterForm());
        }
        return "auth/register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,
                         RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Logout successful");
        return "redirect:/home";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginForm") LoginForm form,
                               BindingResult bindingResult,
                               HttpSession session) {
        if(bindingResult.hasErrors()) {
            return "auth/login";
        }
        AccountViewModel accountLogin = authService.login(form);
        session.setAttribute("LOGGED_IN_USER", accountLogin);
        return redirectByRole(accountLogin, session);
    }

    /**
     * Sau khi dang nhap: dua nguoi dung ve dung khu vuc cua vai tro.
     * ADMIN -> trang quan tri, DRIVER -> cong tai xe.
     * CUSTOMER -> quay lai trang dang dinh vao truoc do (vd dang chon ghe thi bi chan),
     * neu khong co thi ve trang chu.
     */
    private String redirectByRole(AccountViewModel account, HttpSession session) {
        String target = (String) session.getAttribute("REDIRECT_AFTER_LOGIN");
        session.removeAttribute("REDIRECT_AFTER_LOGIN");

        if (account.role() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        if (account.role() == Role.DRIVER) {
            return "redirect:/driver/trips";
        }
        return "redirect:" + (target != null ? target : "/home");
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("registerForm") RegisterForm form,
                                  BindingResult bindingResult,
                                  HttpSession session) {
        if(bindingResult.hasErrors()) {
            return "auth/register";
        }
        AccountViewModel accountRegister = authService.register(form);
        session.setAttribute("LOGGED_IN_USER", accountRegister);
        return redirectByRole(accountRegister, session);
    }


}
