package com.trains.tickets.controller;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.ScheduleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.StopRepository;
import com.trains.tickets.service.ScheduleService;
import com.trains.tickets.service.StationService;
import com.trains.tickets.service.StopService;
import com.trains.tickets.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
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
    private final UserService userService;

    public StopsController(StopRepository stopRepository, StopService stopService,
                           ScheduleRepository scheduleRepository, ScheduleService scheduleService,
                           StationRepository stationRepository, StationService stationService, UserService userService) {
        this.stopRepository = stopRepository;
        this.stopService = stopService;
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.userService = userService;
    }

    @GetMapping
    public String stopList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("stops", stopService.convertAllEntityToDto(stopRepository.findAll()));
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
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
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));

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
                               @RequestParam String timeBegining,
                               @RequestParam String timeEnd,
                               @RequestParam String schedule,
                               @RequestParam String station,
                               @RequestParam Integer stopId,
                               @RequestParam Map<String, String> form,
                               //@RequestParam("stopId") Stop stopChanged,
                               Model model){
        String[] fullTimeBegining = timeBegining.split(":");
        Integer hourOfBegining = Integer.valueOf(fullTimeBegining[0]);
        Integer minuteOfBegining = Integer.valueOf(fullTimeBegining[1]);
        LocalTime localTimeBegining = LocalTime.of(hourOfBegining, minuteOfBegining, 0);
        String[] fullTimeEnd = timeEnd.split(":");
        Integer hourOfEnd = Integer.valueOf(fullTimeEnd[0]);
        Integer minuteOfEnd = Integer.valueOf(fullTimeEnd[1]);
        LocalTime localTimeEnd = LocalTime.of(hourOfEnd, minuteOfEnd, 0);

        if (stopId.equals(0)) {
            Stop stopChanged = new Stop(
                localTimeBegining,
                localTimeEnd,
                scheduleRepository.findByTime(schedule),
                stationRepository.findByName(station)
            );
            stopRepository.save(stopChanged);
        } else {
            Stop stopChanged = stopRepository.findById(stopId);
            boolean wasChanged = false;
            if(!stopChanged.getTimeBegining().equals(localTimeBegining)){
                stopChanged.setTimeBegining(localTimeBegining);
                wasChanged = true;
            }
            if(!stopChanged.getTimeEnd().equals(localTimeEnd)){
                stopChanged.setTimeEnd(localTimeEnd);
                wasChanged = true;
            }
            Schedule scheduleNew = scheduleRepository.findByTime(schedule);
            if(!stopChanged.getSchedule().equals(scheduleNew)){
                stopChanged.setSchedule(scheduleNew);
                wasChanged = true;
            }
            Station stationNew = stationRepository.findByName(station);
            if(!stopChanged.getStation().equals(stationNew)){
                stopChanged.setStation(stationNew);
                wasChanged = true;
            }
            if(wasChanged){
                stopRepository.save(stopChanged);
            }
        }

        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/stops";
    }
}
