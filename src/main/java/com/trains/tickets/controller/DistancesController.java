package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.DistanceRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.DistancesService;
import com.trains.tickets.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/distances")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class DistancesController {
    private final DistanceRepository distanceRepository;
    private final DistancesService distancesService;
    private final StationService stationService;
    private final StationRepository stationRepository;

    public DistancesController(DistanceRepository distanceRepository, DistancesService distancesService, StationService stationService, StationRepository stationRepository) {
        this.distanceRepository = distanceRepository;
        this.distancesService = distancesService;
        this.stationService = stationService;
        this.stationRepository = stationRepository;
    }

    @GetMapping
    public String distanceList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("distances", distancesService.convertAllEntityToDto(distanceRepository.findAll()));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "distancesList";
    }

    @GetMapping("{distance}")
    public String distanceEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String distance,
                               Model model) {
        if (distance.equals("new")) {
            model.addAttribute("distance", distancesService.getEmptyDto());
        } else {
            model.addAttribute("distance", distancesService.convertEntityToDto(distanceRepository.findById(Integer.parseInt(distance))));
        }
        model.addAttribute("user", user);
        model.addAttribute("stations", stationService.convertAllEntitysToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "distancesEdit";
    }
    @PostMapping
    public String distanceSave(@AuthenticationPrincipal User user,
                           @RequestParam String login,
                           @RequestParam Map<String, String> form,
                           @RequestParam("distanceId") Distance distanceChanged,
                           Model model){
        //distanceChanged.setLogin(login);
        distanceRepository.save(distanceChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/distance";
    }
}
