package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.StopsForMainDTO;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class TicketsSearchController {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    @Autowired
    private final StationService stationService;

    public TicketsSearchController(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, StationRepository stationRepository, StationService stationService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    @GetMapping("/ticketsSearch")
    public String main(@AuthenticationPrincipal User user,
                       Map<String, Object> model){
        Iterable<Role> role = roleRepository.findAll();
        Iterable<Station> stations = stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        model.put("roles", role);
        model.put("user", user);
        model.put("stations", stationService.convertEntityToDto(stations));
        if(user.isAdmin()) {
            model.put("adminRole", true);
        }
        if(user.isOperator()) {
            model.put("operatorRole", true);
        }
        return "ticketsSearch";
    }

    @GetMapping("chooseTickets")
    public String chooseTickets(@AuthenticationPrincipal User user,
                                @RequestParam String stationFirst,
                                @RequestParam String stationLast,
                                Map<String, Object> model){
        if(user.isAdmin()) {
            model.put("adminRole", true);
        }
        if(user.isOperator()) {
            model.put("operatorRole", true);
        }
        return "main";
    }
}
