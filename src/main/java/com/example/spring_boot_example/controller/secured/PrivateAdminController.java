package com.example.spring_boot_example.controller.secured;

import com.example.spring_boot_example.entity.User;
import com.example.spring_boot_example.entity.UserRole;
import com.example.spring_boot_example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

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

        if (user.isSuperAdmin()){
            List<User> candidatesToDelete = userService.findAllByRole(Arrays.asList(UserRole.USER, UserRole.ADMIN));
            List<User> candidatesToUpgrade = candidatesToDelete.stream()
                    .filter(User::isSimpleUser)
                    .toList();
            model.addAttribute("candidatesToDelete", candidatesToDelete);
            model.addAttribute("candidatesToUpgrade", candidatesToUpgrade);
        }else {
            List<User> candidatesToDelete = userService.findAllByRole(Collections.singleton(UserRole.USER));
            model.addAttribute("candidatesToDelete", candidatesToDelete);
        }
        return "private/admin/management-page";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam(name = "id") int id){
        User currentUser = userService.getCurrentUser();
        Optional<User> userToBeDeletedOptional = userService.findById(id);
        if (userToBeDeletedOptional.isEmpty()) {
            return "redirect:/admin";
        }
        User userToBeDeleted = userToBeDeletedOptional.get();
        if (userToBeDeleted.isSuperAdmin()) return "redirect:/admin";
        if (userToBeDeleted.isAdmin()
                && !currentUser.isSuperAdmin()) return "redirect:/admin";
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
