package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.PassengerRepository;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.MailSender;
import com.trains.tickets.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.UUID;

@Controller
public class RegistrationController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PassengerRepository passengerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final UserService userService;

    public RegistrationController(UserRepository userRepository, RoleRepository roleRepository, PassengerRepository passengerRepository, PasswordEncoder passwordEncoder, MailSender mailSender, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passengerRepository = passengerRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        return userService.addUser(user, model);
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if(isActivated){
            model.addAttribute("message", "User successfully activated");
        } else{
            model.addAttribute("message", "Activation cod not found");
        }
        return "login";
    }
}
