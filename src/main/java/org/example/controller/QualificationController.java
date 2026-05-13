package org.example.controller;

import org.example.model.Qualification;
import org.example.model.User;
import org.example.service.QualificationService;
import org.example.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/qualifications")
public class QualificationController {

    private final QualificationService qualificationService;
    private final UserService userService;

    public QualificationController(QualificationService qualificationService, UserService userService) {
        this.qualificationService = qualificationService;
        this.userService = userService;
    }

    @GetMapping("")
    public String listQualifications(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName()).get();
        model.addAttribute("qualifications", qualificationService.findByUser(user));
        return "qualifications/list";
    }

    @GetMapping("/{id}")
    public String viewQualification(@PathVariable Long id, Model model) {
        Qualification qualification = qualificationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Qualification not found"));
        model.addAttribute("qualification", qualification);
        return "qualifications/detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteQualification(@PathVariable Long id) {
        qualificationService.deleteQualification(id);
        return "redirect:/qualifications";
    }
}

