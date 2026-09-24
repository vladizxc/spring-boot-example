package com.example.spring_boot_example.controller;


import com.example.spring_boot_example.entity.RecordStatus;
import com.example.spring_boot_example.entity.dto.RecordsContainerDto;
import com.example.spring_boot_example.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommonController {

    private final RecordService recordService;

    @Autowired
    public CommonController(RecordService recordService){
        this.recordService = recordService;
    }

    @RequestMapping("/")
    public String redirectToHomePage(){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String getMainPage(Model model, @RequestParam(name="filter", required = false) String filterMode){
        RecordsContainerDto container = recordService.findAllRecords(filterMode);

        model.addAttribute("numberOfDoneRecords", container.getNumberOfDoneRecords());
        model.addAttribute("numberOfActiveRecords", container.getNumberOfActiveRecords());
        model.addAttribute("records", container.getRecords());

        return "main-page";
    }

    @RequestMapping(value = "/add-record", method = RequestMethod.POST)
    public String addRecord(@RequestParam String title){
        recordService.saveRecord(title);
        return "redirect:/home";
    }

    @RequestMapping(value = "/make-record-done", method = RequestMethod.POST)
    public String makeRecordDone(QueryParameters parameters){
        recordService.setRecordStatus(parameters.getId(), RecordStatus.DONE);
        return "redirect:/home" + (!parameters.getFilter().isBlank() && parameters.getFilter() != null ? "?filter=" + parameters.getFilter() : "");
    }

    @RequestMapping(value = "/delete-record", method = RequestMethod.POST)
    public String deleteRecord(QueryParameters parameters){
        recordService.deleteRecord(parameters.getId());
        return "redirect:/home" + (!parameters.getFilter().isBlank() && parameters.getFilter() != null ? "?filter=" + parameters.getFilter() : "");
    }

}
