package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.StopRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.StationService;
import com.trains.tickets.service.StopService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/arrivals")
public class ArrivalsController {
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final StopRepository stopRepository;
    private final StopService stopService;
    private final MainService mainService;

    public ArrivalsController(StationRepository stationRepository, StationService stationService, StopRepository stopRepository, StopService stopService, MainService mainService) {
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.stopRepository = stopRepository;
        this.stopService = stopService;
        this.mainService = mainService;
    }

    @GetMapping
    public String arrivalsList(@AuthenticationPrincipal User user,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("stations", stationService.convertAllEntityToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        model.addAttribute("arrivals", stopService.convertAllEntityToDtoForArrival(stopRepository.findAll()));
        return "arrivals";
    }
}
