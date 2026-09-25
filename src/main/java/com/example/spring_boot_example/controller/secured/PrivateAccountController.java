package com.example.spring_boot_example.controller.secured;


import com.example.spring_boot_example.controller.QueryParameters;
import com.example.spring_boot_example.entity.RecordStatus;
import com.example.spring_boot_example.entity.dto.RecordsContainerDto;
import com.example.spring_boot_example.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class PrivateAccountController {

    private final RecordService recordService;

    @Autowired
    public PrivateAccountController(RecordService recordService){
        this.recordService = recordService;
    }

    @GetMapping
    public String getMainPage(Model model, @RequestParam(name="filter", required = false) String filterMode){
        RecordsContainerDto container = recordService.findAllRecords(filterMode);

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
