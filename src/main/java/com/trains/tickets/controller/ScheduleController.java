package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.ScheduleRepository;
import com.trains.tickets.repository.TrainRepository;
import com.trains.tickets.service.ScheduleService;
import com.trains.tickets.service.TrainService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/schedule")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class ScheduleController {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;
    private final TrainRepository trainRepository;
    private final TrainService trainService;

    public ScheduleController(ScheduleRepository scheduleRepository, ScheduleService scheduleService,
                              TrainRepository trainRepository, TrainService trainService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.trainRepository = trainRepository;
        this.trainService = trainService;
    }

    @GetMapping
    public String scheduleList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("schedule", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "scheduleList";
    }

    @GetMapping("{schedule}")
    public String scheduleEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String schedule,
                               Model model){
        if (schedule.equals("new")) {
            model.addAttribute("schedule", scheduleService.getEmptyDto());
        } else {
            model.addAttribute("schedule", scheduleService.convertEntityToDto(scheduleRepository.findById(Integer.parseInt(schedule))));
        }
        model.addAttribute("user", user);
        model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "scheduleEdit";
    }
    @PostMapping
    public String scheduleSave(@AuthenticationPrincipal User user,
                           @RequestParam String login,
                           @RequestParam Map<String, String> form,
                           @RequestParam("scheduleId") Schedule scheduleChanged,
                           Model model){
        //distanceChanged.setLogin(login);
        scheduleRepository.save(scheduleChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/schedule";
    }
}
