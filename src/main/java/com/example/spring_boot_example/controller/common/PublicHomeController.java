package com.example.spring_boot_example.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicHomeController {

    @GetMapping("/")
    public String getHomePage(){
        return "public/home-page";
    }
}
