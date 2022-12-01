package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.Ticket;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.TicketRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
    private final TicketRepository ticketRepository;

    public TicketsController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("tickets", ticketRepository.findAll());
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
        model.addAttribute("ticket", ticketRepository.findById(Integer.parseInt(ticket)));
        model.addAttribute("user", user);
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
