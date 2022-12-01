package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Stop;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.StopRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/stops")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class StopsController {
    private final StopRepository stopRepository;

    public StopsController(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    @GetMapping
    public String stopList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("stops", stopRepository.findAll());
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "stopsList";
    }

    @GetMapping("{stop}")
    public String stopEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String stop,
                                   Model model){
        model.addAttribute("stop", stopRepository.findById(Integer.parseInt(stop)));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "stopsEdit";
    }
    @PostMapping
    public String stopSave(@AuthenticationPrincipal User user,
                               @RequestParam String login,
                               @RequestParam Map<String, String> form,
                               @RequestParam("stopId") Stop stopChanged,
                               Model model){
        //distanceChanged.setLogin(login);
        stopRepository.save(stopChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/stops";
    }
}
