package org.example.controller;

import org.example.model.User;
import org.example.model.VerificationStatus;
import org.example.service.QualificationService;
import org.example.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/verificador")
public class VerificadorController {

    private final UserService userService;
    private final QualificationService qualificationService;

    public VerificadorController(UserService userService, QualificationService qualificationService) {
        this.userService = userService;
        this.qualificationService = qualificationService;
    }

    @GetMapping("/dashboard")
    public String verificadorDashboard(Model model, Authentication auth) {
        model.addAttribute("unverifiedUsers", userService.findUnverifiedUsers());
        model.addAttribute("pendingQualifications", qualificationService.findAllUnverified());
        model.addAttribute("totalPendingUsers", userService.findUnverifiedUsers().size());
        model.addAttribute("totalPendingQualifications", qualificationService.getPendingQualifications());
        model.addAttribute("username", auth.getName());
        return "verificador/dashboard";
    }

    @GetMapping("/users")
    public String verifyUsers(Model model) {
        model.addAttribute("users", userService.findUnverifiedUsers());
        return "verificador/users";
    }

    @GetMapping("/user/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("qualifications", qualificationService.findByUser(user));
        return "verificador/user-detail";
    }

    @PostMapping("/user/{id}/verify")
    public String verifyUser(@PathVariable Long id, @RequestParam VerificationStatus status) {
        userService.verifyUser(id, status);
        return "redirect:/verificador/users";
    }

    @GetMapping("/qualifications")
    public String verifyQualifications(Model model) {
        model.addAttribute("qualifications", qualificationService.findAllUnverified());
        return "verificador/qualifications";
    }

    @PostMapping("/qualification/{id}/verify")
    public String verifyQualification(@PathVariable Long id) {
        qualificationService.verifyQualification(id);
        return "redirect:/verificador/qualifications";
    }
}

