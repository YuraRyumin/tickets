package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.StationService;
import com.trains.tickets.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stations")
@PreAuthorize("hasAuthority('operator')")
public class StationsController {
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final UserService userService;
    private final MainService mainService;

    public StationsController(StationRepository stationRepository, StationService stationService, UserService userService, MainService mainService) {
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.userService = userService;
        this.mainService = mainService;
    }

    @GetMapping
    public String stationList(@AuthenticationPrincipal User user,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("stations", stationService.convertAllEntityToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        return "stationsList";
    }

    @GetMapping("{station}")
    public String stationEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String station,
                                   Model model){
        mainService.putUserInfoToModel(user, model);
        stationService.putInfoAboutStationToModel(station, model);
        return "stationsEdit";
    }
    @PostMapping
    public String stationSave(@AuthenticationPrincipal User user,
                               @RequestParam String name,
                               @RequestParam Integer stationId,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        stationService.saveStation(name, stationId, user);
        return "redirect:/stations";
    }
}
