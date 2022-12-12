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

import java.util.Map;

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
        try{
            mainService.putUserInfoToModel(user, model);
            model.addAttribute("schedule", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
            return "scheduleList";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }

    @GetMapping("{schedule}")
    public String scheduleEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String schedule,
                               Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            scheduleService.putInfoAboutScheduleToModel(schedule, model);
            return "scheduleEdit";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }
    @PostMapping
    public String scheduleSave(@AuthenticationPrincipal User user,
                           @RequestParam String time,
                           @RequestParam Integer dayOfWeek,
                           @RequestParam String train,
                           @RequestParam Integer scheduleId,
                           Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            scheduleService.saveSchedule(time, dayOfWeek, train, scheduleId);
            return "redirect:/schedule";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }
}
