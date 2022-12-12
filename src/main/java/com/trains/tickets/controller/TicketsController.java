package com.trains.tickets.controller;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.*;
import com.trains.tickets.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
    private final MainService mainService;
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;

    public TicketsController(MainService mainService, TicketRepository ticketRepository, TicketService ticketService) {
        this.mainService = mainService;
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            model.addAttribute("tickets", ticketService.convertAllEntityToDto(ticketRepository.findAll()));
            return "ticketsList";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }

    @GetMapping("{ticket}")
    public String userEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String ticket,
                               Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            ticketService.putInfoAboutTicketToModel(ticket, model);
            return "ticketsEdit";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
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
        try{
            mainService.putUserInfoToModel(userThis, model);
            ticketService.saveTicket(passenger, dateTicket, train, wagon, price, schedule, ticketId, seat, user, userThis);
            return "redirect:/tickets";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }
}
