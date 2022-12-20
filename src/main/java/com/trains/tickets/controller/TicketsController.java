package com.trains.tickets.controller;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.*;
import com.trains.tickets.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
    private final MainService mainService;
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;
    private final TrainService trainService;
    private final TrainRepository trainRepository;

    public TicketsController(MainService mainService, TicketRepository ticketRepository, TicketService ticketService, ScheduleService scheduleService, ScheduleRepository scheduleRepository, TrainService trainService, TrainRepository trainRepository) {
        this.mainService = mainService;
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
        this.trainService = trainService;
        this.trainRepository = trainRepository;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("tickets", ticketService.convertAllEntityToDto(ticketRepository.findAll()));
        model.addAttribute("schedules", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
        model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll()));
        model.addAttribute("dateNow", LocalDate.now());
        return "ticketsList";
    }

    @GetMapping("{ticket}")
    public String userEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String ticket,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        ticketService.putInfoAboutTicketToModel(ticket, model);
        return "ticketsEdit";
    }

    @PostMapping
    public String ticketSave(@AuthenticationPrincipal User userThis,
                           @RequestParam String passenger,
                           @RequestParam String dateTicket,
                           @RequestParam String train,
                           @RequestParam String wagon,
                           @RequestParam Integer price,
                           @RequestParam String schedule,
                           @RequestParam Integer ticketId,
                           @RequestParam Integer seat,
                           @RequestParam String user,
                           Model model){
        mainService.putUserInfoToModel(userThis, model);
        ticketService.saveTicket(passenger, dateTicket, train, wagon, price, schedule, ticketId, seat, user, userThis);
        return "redirect:/tickets";
    }
}
