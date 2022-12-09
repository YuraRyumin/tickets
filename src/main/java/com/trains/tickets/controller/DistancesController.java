package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.DistanceRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.service.DistancesService;
import com.trains.tickets.service.StationService;
import com.trains.tickets.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/distances")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class DistancesController {
    private final DistanceRepository distanceRepository;
    private final DistancesService distancesService;
    private final StationService stationService;
    private final StationRepository stationRepository;
    private final UserService userService;

    public DistancesController(DistanceRepository distanceRepository, DistancesService distancesService, StationService stationService, StationRepository stationRepository, UserService userService) {
        this.distanceRepository = distanceRepository;
        this.distancesService = distancesService;
        this.stationService = stationService;
        this.stationRepository = stationRepository;
        this.userService = userService;
    }

    @GetMapping
    public String distanceList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("distances", distancesService.convertAllEntityToDto(distanceRepository.findAll()));
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "distancesList";
    }

    @GetMapping("{distance}")
    public String distanceEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String distance,
                               Model model) {
        if (distance.equals("new")) {
            model.addAttribute("distance", distancesService.getEmptyDto());
            model.addAttribute("stationsFirst", stationService.convertAllEntitysToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
            model.addAttribute("stationsLast", stationService.convertAllEntitysToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        } else {
            Distance selectedDistance = distanceRepository.findById(Integer.parseInt(distance));
            model.addAttribute("distance", distancesService.convertEntityToDto(selectedDistance));
            model.addAttribute("stationsFirst", stationService.convertAllEntityToDtoWithSelected(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedDistance.getStationFirst()));
            model.addAttribute("stationsLast", stationService.convertAllEntityToDtoWithSelected(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedDistance.getStationLast()));
        }
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));

        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "distancesEdit";
    }
    @PostMapping
    public String distanceSave(@AuthenticationPrincipal User user,
                           @RequestParam String stationFirst,
                           @RequestParam String stationLast,
                           @RequestParam Integer distanceId,
                           @RequestParam Integer kilometers,
                           @RequestParam Map<String, String> form,
                           //@RequestParam("distanceId") Distance distanceChanged,
                           Model model) {
        if (distanceId.equals(0)) {
            Distance distanceChanged = new Distance(
                    stationRepository.findByName(stationFirst),
                    stationRepository.findByName(stationLast),
                    kilometers
            );
            distanceRepository.save(distanceChanged);
        } else {
            Distance distanceChanged = distanceRepository.findById(distanceId);
            boolean wasChanged = false;
            Station stationFirstNew = stationRepository.findByName(stationFirst);
            if(!distanceChanged.getStationFirst().getName().equals(stationFirstNew.getName())){
                distanceChanged.setStationFirst(stationFirstNew);
                wasChanged = true;
            }
            Station stationLastNew = stationRepository.findByName(stationLast);
            if(!distanceChanged.getStationFirst().getName().equals(stationFirstNew.getName())){
                distanceChanged.setStationLast(stationLastNew);
                wasChanged = true;
            }
            if(!distanceChanged.getKilometers().equals(kilometers)){
                distanceChanged.setKilometers(kilometers);
                wasChanged = true;
            }
            if(wasChanged){
                distanceRepository.save(distanceChanged);
            }
        }
                    //distanceChanged.setLogin(login);
        //distanceRepository.save(distanceChanged);

        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/distances";
    }
}
