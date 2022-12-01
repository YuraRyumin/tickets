package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Train;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.TrainRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/trains")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class TrainsController {
    private final TrainRepository trainRepository;

    public TrainsController(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @GetMapping
    public String trainList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("trains", trainRepository.findAll());
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "trainsList";
    }

    @GetMapping("{train}")
    public String trainEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String train,
                                   Model model){
        model.addAttribute("train", trainRepository.findById(Integer.parseInt(train)));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "trainsEdit";
    }
    @PostMapping
    public String trainSave(@AuthenticationPrincipal User user,
                               @RequestParam String login,
                               @RequestParam Map<String, String> form,
                               @RequestParam("distanceId") Train trainChanged,
                               Model model){
        //distanceChanged.setLogin(login);
        trainRepository.save(trainChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/trains";
    }
}
