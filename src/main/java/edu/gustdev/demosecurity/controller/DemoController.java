package edu.gustdev.demosecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    @GetMapping("/")
    public String showHomePage() {
        return "home";
    }

    //Adding another request mapping for leaders: /leaders
    @GetMapping("/leaders")
    public String showLeaders() {
        return "leaders";
    }

    @GetMapping("/systems")
    public String showSystem() {
        return "systems";
    }

    @GetMapping("/access-denied")
    public String denied() {
        return "access-denied";
    }

}