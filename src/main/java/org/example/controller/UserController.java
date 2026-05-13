package org.example.controller;

import org.example.model.*;
import org.example.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final LocationService locationService;
    private final ReservationService reservationService;
    private final QualificationService qualificationService;

    public UserController(UserService userService, CourseService courseService, ActivityService activityService,
                         LocationService locationService, ReservationService reservationService,
                         QualificationService qualificationService) {
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
        this.locationService = locationService;
        this.reservationService = reservationService;
        this.qualificationService = qualificationService;
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName()).get();
        model.addAttribute("user", user);
        model.addAttribute("reservations", reservationService.findByUser(user));
        model.addAttribute("qualifications", qualificationService.findByUser(user));
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String userProfile(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName()).get();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/add-qualification")
    public String addQualificationPage() {
        return "user/add-qualification";
    }

    @PostMapping("/add-qualification")
    public String addQualification(
            @RequestParam String title,
            @RequestParam String issuer,
            @RequestParam String issueDate,
            @RequestParam(required = false) String description,
            Authentication auth) {

        User user = userService.findByUsername(auth.getName()).get();
        qualificationService.createQualification(user, title, issuer, LocalDate.parse(issueDate), description);
        return "redirect:/user/dashboard";
    }
}

