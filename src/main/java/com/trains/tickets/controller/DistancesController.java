package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.DistanceRepository;
import com.trains.tickets.service.DistancesService;
import com.trains.tickets.service.MainService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/distances")
@PreAuthorize("hasAuthority('operator')")
public class DistancesController {
    private final DistanceRepository distanceRepository;
    private final DistancesService distancesService;
    private final MainService mainService;

    public DistancesController(DistanceRepository distanceRepository, DistancesService distancesService, MainService mainService) {
        this.distanceRepository = distanceRepository;
        this.distancesService = distancesService;
        this.mainService = mainService;
    }

    @GetMapping
    public String distanceList(@AuthenticationPrincipal User user,
                           Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("distances", distancesService.convertAllEntityToDto(distanceRepository.findAll()));
        return "distancesList";
    }

    @GetMapping("{distance}")
    public String distanceEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String distance,
                               Model model) {
        mainService.putUserInfoToModel(user, model);
        distancesService.putInfoAboutDistanceToModel(user, model, distance);
        return "distancesEdit";
    }
    @PostMapping
    public String distanceSave(@AuthenticationPrincipal User user,
                           @RequestParam String stationFirst,
                           @RequestParam String stationLast,
                           @RequestParam Integer distanceId,
                           @RequestParam Integer kilometers,
                           Model model) {
        mainService.putUserInfoToModel(user, model);
        distancesService.saveDistance(distanceId, kilometers, stationFirst, stationLast);
        return "redirect:/distances";
    }
}
