package com.trains.tickets.controller;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Train;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
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
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            model.addAttribute("schedule", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
            return "scheduleList";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.addAttribute("error", errorDTO);
            return "error";
        }
    }

    @GetMapping("{schedule}")
    public String scheduleEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String schedule,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if (schedule.equals("new")) {
                model.addAttribute("schedule", scheduleService.getEmptyDto());
                model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
            } else {
                Schedule selectedSchedule = scheduleRepository.findById(Integer.parseInt(schedule));
                if(selectedSchedule == null){
                    throw  new NullPointerException("Schedule not found!");
                }
                model.addAttribute("schedule", scheduleService.convertEntityToDto(selectedSchedule));
                model.addAttribute("trains", trainService.convertAllEntityToDtoWithSelected(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number")), selectedSchedule.getTrain()));
            }
            return "scheduleEdit";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.addAttribute("error", errorDTO);
            return "error";
        }
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
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
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
            return "redirect:/schedule";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.addAttribute("error", errorDTO);
            return "error";
        }
    }
}
