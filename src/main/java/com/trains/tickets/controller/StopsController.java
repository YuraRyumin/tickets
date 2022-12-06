package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Stop;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.ScheduleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.StopRepository;
import com.trains.tickets.service.ScheduleService;
import com.trains.tickets.service.StationService;
import com.trains.tickets.service.StopService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/stops")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class StopsController {
    private final StopRepository stopRepository;
    private final StopService stopService;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;
    private final StationRepository stationRepository;
    private final StationService stationService;

    public StopsController(StopRepository stopRepository, StopService stopService,
                           ScheduleRepository scheduleRepository, ScheduleService scheduleService,
                           StationRepository stationRepository, StationService stationService) {
        this.stopRepository = stopRepository;
        this.stopService = stopService;
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    @GetMapping
    public String stopList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("stops", stopService.convertAllEntityToDto(stopRepository.findAll()));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "stopsList";
    }

    @GetMapping("{stop}")
    public String stopEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String stop,
                                   Model model){
        if (stop.equals("new")) {
            model.addAttribute("stop", stopService.getEmptyDto());
            model.addAttribute("stations", stationService.convertAllEntityToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
            model.addAttribute("schedule", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
        } else {
            Stop selectedStop = stopRepository.findById(Integer.parseInt(stop));
            model.addAttribute("stop", stopService.convertEntityToDto(selectedStop));
            model.addAttribute("stations", stationService.convertAllEntityToDtoWithSelected(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedStop.getStation()));
            model.addAttribute("schedule", scheduleService.convertAllEntityToDtoWithSelected(scheduleRepository.findAll(), selectedStop.getSchedule()));
        }
        model.addAttribute("user", user);

        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "stopsEdit";
    }
    @PostMapping
    public String stopSave(@AuthenticationPrincipal User user,
                               @RequestParam String login,
                               @RequestParam Map<String, String> form,
                               @RequestParam("stopId") Stop stopChanged,
                               Model model){
        //distanceChanged.setLogin(login);
        stopRepository.save(stopChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/stops";
    }
}
