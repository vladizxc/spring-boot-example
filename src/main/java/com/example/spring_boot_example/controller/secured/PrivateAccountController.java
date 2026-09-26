package com.example.spring_boot_example.controller.secured;


import com.example.spring_boot_example.controller.QueryParameters;
import com.example.spring_boot_example.entity.RecordStatus;
import com.example.spring_boot_example.entity.User;
import com.example.spring_boot_example.entity.dto.RecordsContainerDto;
import com.example.spring_boot_example.service.RecordService;
import com.example.spring_boot_example.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class PrivateAccountController {

    private final UserService userService;
    private final RecordService recordService;

    @Autowired
    public PrivateAccountController(RecordService recordService, UserService userService){
        this.userService = userService;
        this.recordService = recordService;
    }

    @GetMapping
    public String getMainPage(HttpServletRequest request, Model model, @RequestParam(name="filter", required = false) String filterMode){
        HttpSession session = request.getSession();
        Object counter = session.getAttribute("visitsCounter");
        if (counter != null){
            model.addAttribute("visitsCounter", (Integer) counter);
            session.setAttribute("visitsCounter", ((Integer) counter) + 1);
        } else {
            model.addAttribute("visitsCounter", 0);
            session.setAttribute("visitsCounter", 1);
        }
        RecordsContainerDto container = recordService.findAllRecords(filterMode);
        model.addAttribute("userName", container.getUserName());
        model.addAttribute("numberOfDoneRecords", container.getNumberOfDoneRecords());
        model.addAttribute("numberOfActiveRecords", container.getNumberOfActiveRecords());
        model.addAttribute("records", container.getRecords());

        return "private/account-page";
    }

    @PostMapping("/add-record")
    public String addRecord(@RequestParam String title){
        recordService.saveRecord(title);
        return "redirect:/account";
    }

    @PostMapping("/make-record-done")
    public String makeRecordDone(QueryParameters parameters){
        recordService.setRecordStatus(parameters.getId(), RecordStatus.DONE);
        return "redirect:/account" + (!parameters.getFilter().isBlank() && parameters.getFilter() != null ? "?filter=" + parameters.getFilter() : "");
    }

    @PostMapping("/delete-record")
    public String deleteRecord(QueryParameters parameters){
        recordService.deleteRecord(parameters.getId());
        return "redirect:/account" + (!parameters.getFilter().isBlank() && parameters.getFilter() != null ? "?filter=" + parameters.getFilter() : "");
    }

}
