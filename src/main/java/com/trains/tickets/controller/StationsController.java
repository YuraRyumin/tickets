package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.StationRepository;
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

    public StationsController(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @GetMapping
    public String stationList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("stations", stationRepository.findAll());
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
        model.addAttribute("stations", stationRepository.findById(Integer.parseInt(station)));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "stationEdit";
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
