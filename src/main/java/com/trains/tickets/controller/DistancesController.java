package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.DistanceRepository;
import com.trains.tickets.repository.UserRepository;
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

    public DistancesController(DistanceRepository distanceRepository) {
        this.distanceRepository = distanceRepository;
    }

    @GetMapping
    public String distanceList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("distances", distanceRepository.findAll());
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
                               Model model){
        model.addAttribute("distance", distanceRepository.findById(Integer.parseInt(distance)));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "userEdit";
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
