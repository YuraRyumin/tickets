package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.DistanceRepository;
import com.trains.tickets.repository.PassengerRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/passengers")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class PassengersController {
    private final PassengerRepository passengerRepository;

    public PassengersController(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @GetMapping
    public String PassengersList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("passengers", passengerRepository.findAll());
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "passengersList";
    }

    @GetMapping("{passenger}")
    public String PassengerEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String passenger,
                               Model model){
        model.addAttribute("passenger", passengerRepository.findById(Integer.parseInt(passenger)));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "passengersEdit";
    }
    @PostMapping
    public String userSave(@AuthenticationPrincipal User user,
                           @RequestParam String login,
                           @RequestParam Map<String, String> form,
                           @RequestParam("passengerId") Passenger passengerChanged,
                           Model model){
        //distanceChanged.setLogin(login);
        passengerRepository.save(passengerChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/passengers";
    }
}
