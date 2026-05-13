package org.example.controller;

import org.example.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final LocationService locationService;
    private final ReservationService reservationService;
    private final QualificationService qualificationService;

    public AdminController(UserService userService, CourseService courseService, ActivityService activityService,
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
    public String adminDashboard(Model model, Authentication auth) {
        // Statistics
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("totalAdmins", userService.getTotalAdmins());
        model.addAttribute("totalVerificadores", userService.getTotalVerificadores());
        model.addAttribute("totalUsuarios", userService.getTotalUsuarios());
        model.addAttribute("pendingVerifications", userService.getPendingVerifications());

        model.addAttribute("totalCourses", courseService.getTotalCourses());
        model.addAttribute("totalActivities", activityService.getTotalActivities());
        model.addAttribute("totalLocations", locationService.getTotalLocations());
        model.addAttribute("totalReservations", reservationService.getTotalReservations());
        model.addAttribute("pendingQualifications", qualificationService.getPendingQualifications());

        model.addAttribute("username", auth.getName());

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/courses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "admin/courses";
    }

    @GetMapping("/activities")
    public String manageActivities(Model model) {
        model.addAttribute("activities", activityService.findAll());
        return "admin/activities";
    }

    @GetMapping("/locations")
    public String manageLocations(Model model) {
        model.addAttribute("locations", locationService.findAll());
        return "admin/locations";
    }

    @GetMapping("/reservations")
    public String manageReservations(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "admin/reservations";
    }
}

