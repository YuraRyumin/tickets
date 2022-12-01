package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.User;
import com.trains.tickets.domain.Wagon;
import com.trains.tickets.repository.WagonRepository;
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

    public WagonsController(WagonRepository wagonRepository) {
        this.wagonRepository = wagonRepository;
    }

    @GetMapping
    public String wagonsList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("wagons", wagonRepository.findAll());
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
        model.addAttribute("wagon", wagonRepository.findById(Integer.parseInt(wagon)));
        model.addAttribute("user", user);
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
