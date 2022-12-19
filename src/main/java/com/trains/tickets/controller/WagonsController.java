package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.WagonRepository;
import com.trains.tickets.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wagons")
@PreAuthorize("hasAuthority('operator')")
public class WagonsController {
    private final WagonRepository wagonRepository;
    private final WagonService wagonService;
    private final MainService mainService;

    public WagonsController(WagonRepository wagonRepository, WagonService wagonService, MainService mainService) {
        this.wagonRepository = wagonRepository;
        this.wagonService = wagonService;
        this.mainService = mainService;
    }

    @GetMapping
    public String wagonsList(@AuthenticationPrincipal User user,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("wagons", wagonService.convertAllEntityToDto(wagonRepository.findAll()));
        return "wagonsList";
    }

    @GetMapping("{wagon}")
    public String wagonEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String wagon,
                                   Model model){
        mainService.putUserInfoToModel(user, model);
        wagonService.putInfoAboutWagonToModel(wagon, model);
        return "wagonsEdit";
    }
    @PostMapping
    public String wagonSave(@AuthenticationPrincipal User user,
                            @RequestParam String train,
                            @RequestParam String serviceClasses,
                            @RequestParam String name,
                            @RequestParam Integer seats,
                            @RequestParam Integer wagonId,
                            Model model){
        mainService.putUserInfoToModel(user, model);
        wagonService.saveWagon(train, serviceClasses, name, seats, wagonId);
        return "redirect:/wagons";
    }
}
