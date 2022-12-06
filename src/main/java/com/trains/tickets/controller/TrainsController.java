package com.trains.tickets.controller;

import com.trains.tickets.domain.Train;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.TrainRepository;
import com.trains.tickets.service.TrainService;
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
    private final TrainService trainService;

    public TrainsController(TrainRepository trainRepository, TrainService trainService) {
        this.trainRepository = trainRepository;
        this.trainService = trainService;
    }

    @GetMapping
    public String trainList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll()));
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
        if (train.equals("new")) {
            model.addAttribute("train", trainService.getEmptyDto());
        } else {
            model.addAttribute("train", trainService.convertEntityToDto(trainRepository.findById(Integer.parseInt(train))));
        }
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
