package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.ServiceClassRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.ServiceClassService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/serviceClasses")
@PreAuthorize("hasAuthority('operator')")
public class ServiceClassesController {
    private final ServiceClassRepository serviceClassRepository;
    private final ServiceClassService serviceClassService;
    private final MainService mainService;

    public ServiceClassesController(ServiceClassRepository serviceClassRepository, ServiceClassService serviceClassService, MainService mainService) {
        this.serviceClassRepository = serviceClassRepository;
        this.serviceClassService = serviceClassService;
        this.mainService = mainService;
    }

    @GetMapping
    public String serviceClassList(@AuthenticationPrincipal User user,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("serviceClasses", serviceClassService.convertAllEntityToDto(serviceClassRepository.findAll()));
        return "serviceClassesList";
    }

    @GetMapping("{serviceClass}")
    public String serviceClassEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String serviceClass,
                                   Model model){
        mainService.putUserInfoToModel(user, model);
        serviceClassService.putInfoAboutServiceClassToModel(serviceClass, model);
        return "serviceClassesEdit";
    }
    @PostMapping
    public String serviceClassSave(@AuthenticationPrincipal User user,
                               @RequestParam String name,
                               @RequestParam Float prisePerKm,
                               @RequestParam Integer serviceClassId,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        serviceClassService.saveServiceClass(name, prisePerKm, serviceClassId, user);
        return "redirect:/serviceClasses";
    }
}
