package com.trains.tickets.controller;

import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.service.StationService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/stations")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class StationsController {
    private final StationRepository stationRepository;
    private final StationService stationService;

    public StationsController(StationRepository stationRepository, StationService stationService) {
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    @GetMapping
    public String stationList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("stations", stationService.convertAllEntityToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "stationsList";
    }

    @GetMapping("{station}")
    public String stationEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String station,
                                   Model model){
        if (station.equals("new")) {
            model.addAttribute("stations", stationService.getEmptyDto());
        } else {
            model.addAttribute("stations", stationService.convertEntityToDto(stationRepository.findById(Integer.parseInt(station))));
        }
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "stationsEdit";
    }
    @PostMapping
    public String stationSave(@AuthenticationPrincipal User user,
                               @RequestParam String login,
                               @RequestParam Map<String, String> form,
                               @RequestParam("stationId") Station stationChanged,
                               Model model){
        //distanceChanged.setLogin(login);
        stationRepository.save(stationChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/stations";
    }
}
