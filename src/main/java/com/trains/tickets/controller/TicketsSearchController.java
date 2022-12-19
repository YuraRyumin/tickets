package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.PassengerService;
import com.trains.tickets.service.RoleService;
import com.trains.tickets.service.StationService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class TicketsSearchController {
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final MainService mainService;
    private final PassengerService passengerService;

    public TicketsSearchController(RoleRepository roleRepository, RoleService roleService, StationRepository stationRepository, StationService stationService, MainService mainService, PassengerService passengerService) {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.mainService = mainService;
        this.passengerService = passengerService;
    }

    @GetMapping("/ticketsSearch")
    public String main(@AuthenticationPrincipal User user,
                       Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("dateNow", LocalDate.now());
        model.addAttribute("roles", roleService.convertAllEntityToDto(roleRepository.findAll()));
        model.addAttribute("stations", stationService.convertAllEntitysToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
//        model.addAttribute("passenger", passengerService.convertEntityToDto(user.getPassenger()));
//        model.addAttribute("genders", passengerService.getGendersList(user.getPassenger()));
        return "ticketsSearch";
    }

}
