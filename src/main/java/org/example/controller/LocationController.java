package org.example.controller;

import org.example.model.Location;
import org.example.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("")
    public String listLocations(Model model) {
        model.addAttribute("locations", locationService.findAll());
        return "locations/list";
    }

    @GetMapping("/{id}")
    public String viewLocation(@PathVariable Long id, Model model) {
        Location location = locationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        model.addAttribute("location", location);
        model.addAttribute("activities", location.getActivities());
        return "locations/detail";
    }
}

