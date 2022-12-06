package com.trains.tickets.controller;

import com.trains.tickets.domain.Ticket;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.*;
import com.trains.tickets.service.*;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final PassengerRepository passengerRepository;
    private final PassengerService passengerService;
    private final TrainRepository trainRepository;
    private final TrainService trainService;
    private final WagonRepository wagonRepository;
    private final WagonService wagonService;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;

    public TicketsController(TicketRepository ticketRepository, TicketService ticketService,
                             PassengerRepository passengerRepository, PassengerService passengerService,
                             TrainRepository trainRepository, TrainService trainService,
                             WagonRepository wagonRepository, WagonService wagonService,
                             ScheduleRepository scheduleRepository, ScheduleService scheduleService) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.wagonRepository = wagonRepository;
        this.wagonService = wagonService;
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("tickets", ticketService.convertAllEntityToDto(ticketRepository.findAll()));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "ticketsList";
    }

    @GetMapping("{ticket}")
    public String userEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String ticket,
                               Model model){
        if (ticket.equals("new")) {
            model.addAttribute("ticket", ticketService.getEmptyDto());
        } else {
            model.addAttribute("ticket", ticketService.convertEntityToDto(ticketRepository.findById(Integer.parseInt(ticket))));
        }
        model.addAttribute("user", user);
        model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
        model.addAttribute("wagons", wagonService.convertAllEntityToDto(wagonRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        model.addAttribute("schedules", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
        model.addAttribute("passengers", passengerService.convertAllEntityToDto(passengerRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));

        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "ticketsEdit";
    }

    @PostMapping
    public String userSave(@AuthenticationPrincipal User user,
                           @RequestParam String login,
                           @RequestParam Map<String, String> form,
                           @RequestParam("ticketId") Ticket ticketChanged,
                           Model model){
        //ticketRepository.setLogin(login);
        ticketRepository.save(ticketChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/tickets";
    }
}
