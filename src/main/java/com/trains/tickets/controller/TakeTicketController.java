package com.trains.tickets.controller;

import com.trains.tickets.domain.*;
import com.trains.tickets.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/takeTicket")
public class TakeTicketController {

    private final MainService mainService;
    private final TakeTicketService takeTicketService;

    public TakeTicketController(MainService mainService, TakeTicketService takeTicketService) {
        this.mainService = mainService;
        this.takeTicketService = takeTicketService;
    }

    @PostMapping
    public String takeTicket(@AuthenticationPrincipal User user,
                             @RequestParam Map<String, String> form,
                             Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            takeTicketService.saveTicket(user, form, model);
            return "redirect:/ticketsSearch";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }


}
