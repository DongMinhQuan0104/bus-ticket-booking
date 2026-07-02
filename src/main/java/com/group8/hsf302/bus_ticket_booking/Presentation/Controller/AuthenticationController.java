package com.group8.hsf302.bus_ticket_booking.Presentation.Controller;

import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.LoginForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Request.RegisterForm;
import com.group8.hsf302.bus_ticket_booking.Application.Dto.Response.AccountViewModel;
import com.group8.hsf302.bus_ticket_booking.Application.Service.Authentication.AuthenticationService;
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
    private final AuthenticationService authservice;


    public AuthenticationController(AuthenticationService authservice) {
        this.authservice = authservice;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Logout successful");
        return "redirect:/index";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginForm") LoginForm form,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model) {
        if(bindingResult.hasErrors()) {
            return "index";
        }
        AccountViewModel accountLogin = authservice.login(form);
        session.setAttribute("LOGGED_IN_USER", accountLogin);
        return "redirect:/index";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("registerForm") RegisterForm form,
                                  BindingResult bindingResult,
                                  HttpSession session,
                                  Model model) {

        System.out.println("===== REGISTER FORM DATA =====");
        System.out.println("Full name: " + form.getFullName());
        System.out.println("Email: " + form.getEmail());
        System.out.println("Phone: " + form.getPhoneNumber());
        System.out.println("Password: " + form.getPassword());
        System.out.println("Confirm password: " + form.getConfirmPassword());

        if (bindingResult.hasErrors()) {
            System.out.println("===== REGISTER VALIDATION ERRORS =====");
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("ERROR: " + error.getDefaultMessage());
            });

            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", form);
            return "index";
        }

        AccountViewModel accountRegister = authservice.register(form);
        session.setAttribute("LOGGED_IN_USER", accountRegister);

        System.out.println("===== REGISTER SUCCESS =====");
        System.out.println("Created account successfully");

        return "redirect:/index";
    }


}
