package com.trains.tickets.controller;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Train;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.ScheduleRepository;
import com.trains.tickets.repository.TrainRepository;
import com.trains.tickets.service.ScheduleService;
import com.trains.tickets.service.TrainService;
import com.trains.tickets.service.UserService;
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
    private final UserService userService;

    public ScheduleController(ScheduleRepository scheduleRepository, ScheduleService scheduleService,
                              TrainRepository trainRepository, TrainService trainService, UserService userService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.userService = userService;
    }

    @GetMapping
    public String scheduleList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("schedule", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
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
            model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
        } else {
            Schedule selectedSchedule = scheduleRepository.findById(Integer.parseInt(schedule));
            model.addAttribute("schedule", scheduleService.convertEntityToDto(selectedSchedule));
            model.addAttribute("trains", trainService.convertAllEntityToDtoWithSelected(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number")), selectedSchedule.getTrain()));
        }
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));

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
                           @RequestParam String time,
                           @RequestParam Integer dayOfWeek,
                           @RequestParam String train,
                           @RequestParam Integer scheduleId,
                           @RequestParam Map<String, String> form,
                           //@RequestParam("scheduleId") Schedule scheduleChanged,
                           Model model){
        if (scheduleId.equals(0)) {
            Schedule scheduleChanged = new Schedule(
                    time,
                    dayOfWeek,
                    trainRepository.findByNumber(train)
            );
            scheduleRepository.save(scheduleChanged);
        } else {
            Schedule scheduleChanged = scheduleRepository.findById(scheduleId);
            boolean wasChanged = false;
            if (!scheduleChanged.getTime().equals(time)) {
                scheduleChanged.setTime(time);
                wasChanged = true;
            }
            if (!scheduleChanged.getDayOfWeek().equals(dayOfWeek)) {
                scheduleChanged.setDayOfWeek(dayOfWeek);
                wasChanged = true;
            }
            Train trainNew = trainRepository.findByNumber(train);
            if (!scheduleChanged.getTrain().equals(trainNew)) {
                scheduleChanged.setTrain(trainNew);
                wasChanged = true;
            }
            if (wasChanged){
                scheduleRepository.save(scheduleChanged);
            }
        }

        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/schedule";
    }
}
