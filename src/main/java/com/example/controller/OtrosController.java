package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OtrosController {

    @GetMapping("/otros")
    public String otros() {
        return "html/otros";
    }
}

