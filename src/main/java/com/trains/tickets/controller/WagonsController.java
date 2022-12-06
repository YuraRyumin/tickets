package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.domain.Wagon;
import com.trains.tickets.repository.ServiceClassRepository;
import com.trains.tickets.repository.TrainRepository;
import com.trains.tickets.repository.WagonRepository;
import com.trains.tickets.service.ServiceClassService;
import com.trains.tickets.service.TrainService;
import com.trains.tickets.service.WagonService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/wagons")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class WagonsController {
    private final WagonRepository wagonRepository;
    private final WagonService wagonService;
    private final TrainRepository trainRepository;
    private final TrainService trainService;
    private final ServiceClassRepository serviceClassRepository;
    private final ServiceClassService serviceClassService;

    public WagonsController(WagonRepository wagonRepository, WagonService wagonService, TrainRepository trainRepository, TrainService trainService, ServiceClassRepository serviceClassRepository, ServiceClassService serviceClassService) {
        this.wagonRepository = wagonRepository;
        this.wagonService = wagonService;
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.serviceClassRepository = serviceClassRepository;
        this.serviceClassService = serviceClassService;
    }

    @GetMapping
    public String wagonsList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("wagons", wagonService.convertAllEntityToDto(wagonRepository.findAll()));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "wagonsList";
    }

    @GetMapping("{wagon}")
    public String wagonEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String wagon,
                                   Model model){
        if (wagon.equals("new")) {
            model.addAttribute("wagon", wagonService.getEmptyDto());
        } else {
            model.addAttribute("wagon", wagonService.convertEntityToDto(wagonRepository.findById(Integer.parseInt(wagon))));
        }

        model.addAttribute("user", user);
        model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
        model.addAttribute("serviceClasses", serviceClassService.convertAllEntityToDto(serviceClassRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));

        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "wagonsEdit";
    }
    @PostMapping
    public String wagonSave(@AuthenticationPrincipal User user,
                               @RequestParam String login,
                               @RequestParam Map<String, String> form,
                               @RequestParam("wagonId") Wagon wagonChanged,
                               Model model){
        //distanceChanged.setLogin(login);
        wagonRepository.save(wagonChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/wagons";
    }
}
