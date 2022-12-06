package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class MainController {
    @Value("${spring.mail.username}")
    private String username;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    @Autowired
    private final StationService stationService;

    public MainController(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, StationRepository stationRepository, StationService stationService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user,
                           Map<String, Object> model) {
        model.put("user", user);
        if(user != null) {
            if (user.isAdmin()) {
                model.put("adminRole", true);
            }
            if (user.isOperator()) {
                model.put("operatorRole", true);
            }
        }
        return "tickets";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       Map<String, Object> model){
        model.put("user", user);
        model.put("stations", stationService.convertAllEntitysToDto(stationRepository.findAll()));
        if(user.isAdmin()) {
            model.put("adminRole", true);
        }
        if(user.isOperator()) {
            model.put("operatorRole", true);
        }
        return "ticketsSearch";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String name,
                      Map<String, Object> model){
        model.put("user", user);
        if(user.isAdmin()) {
            model.put("adminRole", true);
        }
        if(user.isOperator()) {
            model.put("operatorRole", true);
        }
        return "ticketsSearch";
    }



    @PostMapping("changePass")
    public String post(@AuthenticationPrincipal User user,
                       Map<String, Object> model){
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        mailMessage.setFrom(username);
//        mailMessage.setTo("s_shukrik_s@mail.ru");
//        mailMessage.setSubject("activation");
//        mailMessage.setText("User successfully activated");
//
//        mailSender.send(mailMessage);
//        model.put("user", user);
        //return "main";
        return "ticketsSearch";
    }
}
