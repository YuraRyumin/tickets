package com.trains.tickets.controller;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.StopRepository;
import com.trains.tickets.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stops")
@PreAuthorize("hasAuthority('operator')")
public class StopsController {
    private final StopRepository stopRepository;
    private final StopService stopService;
    private final MainService mainService;

    public StopsController(StopRepository stopRepository, StopService stopService, MainService mainService) {
        this.stopRepository = stopRepository;
        this.stopService = stopService;
        this.mainService = mainService;
    }

    @GetMapping
    public String stopList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("stops", stopService.convertAllEntityToDto(stopRepository.findAll()));
        mainService.putUserInfoToModel(user, model);
        return "stopsList";
    }

    @GetMapping("{stop}")
    public String stopEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String stop,
                                   Model model){
        mainService.putUserInfoToModel(user, model);
        stopService.putInfoAboutStopToModel(stop, model);
        return "stopsEdit";
    }
    @PostMapping
    public String stopSave(@AuthenticationPrincipal User user,
                               @RequestParam String timeBegining,
                               @RequestParam String timeEnd,
                               @RequestParam String schedule,
                               @RequestParam String station,
                               @RequestParam Integer stopId,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        stopService.saveStop(timeBegining, timeEnd, schedule, station, stopId);
        return "redirect:/stops";
    }
}
