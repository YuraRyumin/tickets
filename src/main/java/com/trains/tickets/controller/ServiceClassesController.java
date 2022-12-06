package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.ServiceClass;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.ServiceClassRepository;
import com.trains.tickets.service.ServiceClassService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/serviceClasses")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class ServiceClassesController {
    private final ServiceClassRepository serviceClassRepository;
    private final ServiceClassService serviceClassService;

    public ServiceClassesController(ServiceClassRepository serviceClassRepository, ServiceClassService serviceClassService) {
        this.serviceClassRepository = serviceClassRepository;
        this.serviceClassService = serviceClassService;
    }

    @GetMapping
    public String serviceClassList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("serviceClasses", serviceClassService.convertAllEntityToDto(serviceClassRepository.findAll()));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "serviceClassesList";
    }

    @GetMapping("{serviceClass}")
    public String serviceClassEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String serviceClass,
                                   Model model){
        if (serviceClass.equals("new")) {
            model.addAttribute("serviceClass", serviceClassService.getEmptyDto());
        } else {
            model.addAttribute("serviceClass", serviceClassService.convertEntityToDto(serviceClassRepository.findById(Integer.parseInt(serviceClass))));
        }
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "serviceClassesEdit";
    }
    @PostMapping
    public String serviceClassSave(@AuthenticationPrincipal User user,
                               @RequestParam String login,
                               @RequestParam Map<String, String> form,
                               @RequestParam("serviceClassId") ServiceClass serviceClassChanged,
                               Model model){
        //distanceChanged.setLogin(login);
        serviceClassRepository.save(serviceClassChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/serviceClasses";
    }
}
