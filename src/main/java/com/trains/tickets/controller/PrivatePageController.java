package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.NewsRepository;
import com.trains.tickets.repository.TicketRepository;
import com.trains.tickets.service.NewsService;
import com.trains.tickets.service.TicketService;
import com.trains.tickets.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/privatePage")
public class PrivatePageController {

    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final NewsRepository newsRepository;
    private final NewsService newsService;
    private final UserService userService;

    public PrivatePageController(TicketRepository ticketRepository, TicketService ticketService, NewsRepository newsRepository, NewsService newsService, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.newsRepository = newsRepository;
        this.newsService = newsService;
        this.userService = userService;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "ticketsList";
    }

    @GetMapping("{uuid}")
    public String userEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String uuid,
                               Model model){
        model.addAttribute("uuid", uuid);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("telephone", user.getTelephone());
        if(user.getRole().equals(null)){
            model.addAttribute("role", "");
        } else {
            model.addAttribute("role", user.getRole().getName());
        }
        if(user.getPassenger().equals(null)){
            model.addAttribute("passenger", "");
        } else {
            model.addAttribute("passenger", user.getPassenger().getName() + " " + user.getPassenger().getSurname());
        }
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        model.addAttribute("tickets", ticketService.convertAllEntityToDto(ticketRepository.findAllByUserLogin(user.getLogin())));
        model.addAttribute("news", newsService.convertAllEntityToDto(newsRepository.findAllByUserUuid(user.getUuid())));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "privatePage";
    }
}
