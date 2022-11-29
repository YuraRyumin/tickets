package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.Stop;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.StationsForMainDTO;
import com.trains.tickets.dto.StopsForMainDTO;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.StopRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.StationService;
import com.trains.tickets.service.TicketFinder;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class TicketsController {
    @Value("${spring.datasource.url}")
    private String urlSql;
    @Value("${spring.datasource.username}")
    private String usernameSql;
    @Value("${spring.datasource.password}")
    private String passwordSql;
    @Value("${spring.mail.username}")
    private String username;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    @Autowired
    private final StationService stationService;

    public TicketsController(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, StationRepository stationRepository, StationService stationService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "tickets";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       Map<String, Object> model){
        Iterable<Station> stations = stationRepository.findAll();
        model.put("user", user);
        model.put("stations", stationService.convertEntityToDto(stations));
        if(user.isAdmin()) {
            model.put("adminRole", true);
        }
        return "ticketsSearch";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String name,
                      Map<String, Object> model){

        Iterable<Role> roleI = roleRepository.findAll();
        model.put("roles", roleI);
        model.put("user", user);

        //return "main";
        return "ticketsSearch";
    }

    @PostMapping("filter")
    public String filter(@AuthenticationPrincipal User user,
                         @RequestParam String filter,
                         Map<String, Object> model){
        if(filter != null && !filter.isEmpty()) {
            Set<Role> rolesSet = new HashSet<>();
            rolesSet.add(roleRepository.findByName(filter));
            model.put("roles", rolesSet);
        } else {
            Iterable<Role>  rolesI = roleRepository.findAll();
            model.put("roles", rolesI);
        }
        model.put("user", user);
        return "main";
    }

    @PostMapping("changePass")
    public String post(@AuthenticationPrincipal User user,
                       Map<String, Object> model){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo("s_shukrik_s@mail.ru");
        mailMessage.setSubject("activation");
        mailMessage.setText("User successfully activated");

        mailSender.send(mailMessage);
        model.put("user", user);
        //return "main";
        return "ticketsSearch";
    }


}
