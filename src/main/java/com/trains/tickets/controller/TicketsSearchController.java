package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.RoleService;
import com.trains.tickets.service.StationService;
import com.trains.tickets.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
public class TicketsSearchController {
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private final StationService stationService;
    private final UserService userService;

    public TicketsSearchController(RoleRepository roleRepository, RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, StationRepository stationRepository, StationService stationService, UserService userService) {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.userService = userService;
    }

    @GetMapping("/ticketsSearch")
    public String main(@AuthenticationPrincipal User user,
                       Map<String, Object> model){
        try{
            model.put("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.put("adminRole", true);
            }
            if(user.isOperator()) {
                model.put("operatorRole", true);
            }
            model.put("dateNow", LocalDate.now());
            model.put("roles", roleService.convertAllEntityToDto(roleRepository.findAll()));
            model.put("stations", stationService.convertAllEntitysToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.put("error", errorDTO);
            return "error";
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
