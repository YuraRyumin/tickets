package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.ScheduleRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.ScheduleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/schedule")
@PreAuthorize("hasAuthority('operator')")
public class ScheduleController {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;
    private final MainService mainService;

    public ScheduleController(ScheduleRepository scheduleRepository, ScheduleService scheduleService, MainService mainService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.mainService = mainService;
    }

    @GetMapping
    public String scheduleList(@AuthenticationPrincipal User user,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("schedule", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
        return "scheduleList";
    }

    @GetMapping("{schedule}")
    public String scheduleEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String schedule,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        scheduleService.putInfoAboutScheduleToModel(schedule, model);
        return "scheduleEdit";
    }
    @PostMapping
    public String scheduleSave(@AuthenticationPrincipal User user,
                           @RequestParam String time,
                           @RequestParam Integer dayOfWeek,
                           @RequestParam String train,
                           @RequestParam Integer scheduleId,
                           Model model){
        mainService.putUserInfoToModel(user, model);
        scheduleService.saveSchedule(time, dayOfWeek, train, scheduleId, user);
        return "redirect:/schedule";
    }
}
