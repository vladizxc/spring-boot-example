package com.example.spring_boot_example.controller.secured;

import com.example.spring_boot_example.entity.User;
import com.example.spring_boot_example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PrivateAdminController {
    private UserService userService;

    @Autowired
    public PrivateAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getManagementPage(Model model){
        User user = userService.getCurrentUser();
        model.addAttribute("userName", user.getName());

        return "private/admin/management-page";
    }
}
