package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.NewsRepository;
import com.trains.tickets.repository.TicketRepository;
import com.trains.tickets.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/privatePage")
public class PrivatePageController {

    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final NewsRepository newsRepository;
    private final NewsService newsService;
    private final UserService userService;
    private final MainService mainService;
    private final PrivatePageService privatePageService;

    public PrivatePageController(TicketRepository ticketRepository, TicketService ticketService, NewsRepository newsRepository, NewsService newsService, UserService userService, MainService mainService, PrivatePageService privatePageService) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.newsRepository = newsRepository;
        this.newsService = newsService;
        this.userService = userService;
        this.mainService = mainService;
        this.privatePageService = privatePageService;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        mainService.putUserInfoToModel(user, model);
        return "ticketsList";
    }

    @GetMapping("{uuid}")
    public String userEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String uuid,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        privatePageService.putInfoAboutPrivatePageToModel(user, model, uuid);
        return "privatePage";
    }
    @PostMapping
    public String passenferSave(@AuthenticationPrincipal User user,
                                @RequestParam String passengerName,
                                @RequestParam String passengerSurname,
                                @RequestParam String passengerPassport,
                                @RequestParam String passengerGender,
                                @RequestParam String passengerDateOfBirth,
                                Model model){
        mainService.putUserInfoToModel(user, model);
        privatePageService.saveInfoAboutPassenger(user,
                                                passengerName,
                                                passengerSurname,
                                                passengerPassport,
                                                passengerGender,
                                                passengerDateOfBirth);
        privatePageService.putInfoAboutPrivatePageToModel(user, model, user.getUuid());
        return "/privatePage/" + user.getUuid();
    }
}
