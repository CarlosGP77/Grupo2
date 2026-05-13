package org.example.controller;

import org.example.model.Activity;
import org.example.service.ActivityService;
import org.example.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.example.model.User;
import org.example.service.UserService;

@Controller
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final ReservationService reservationService;
    private final UserService userService;

    public ActivityController(ActivityService activityService, ReservationService reservationService, UserService userService) {
        this.activityService = activityService;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping("")
    public String listActivities(Model model) {
        model.addAttribute("activities", activityService.findUpcomingActivities());
        return "activities/list";
    }

    @GetMapping("/{id}")
    public String viewActivity(@PathVariable Long id, Model model, Authentication auth) {
        Activity activity = activityService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
        model.addAttribute("activity", activity);
        model.addAttribute("availableSpots", activity.getAvailableSpots());
        model.addAttribute("isAvailable", activity.isAvailable());

        if (auth != null) {
            User user = userService.findByUsername(auth.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("userReservation", reservationService.findByUserAndActivity(user, activity).orElse(null));
            }
        }

        return "activities/detail";
    }

    @PostMapping("/{id}/reserve")
    public String reserveActivity(@PathVariable Long id, Authentication auth) {
        Activity activity = activityService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
        User user = userService.findByUsername(auth.getName()).get();

        try {
            reservationService.createReservation(user, activity);
        } catch (IllegalArgumentException e) {
            return "redirect:/activities/" + id + "?error=" + e.getMessage();
        }

        return "redirect:/user/dashboard";
    }
}

