package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.PassengerRepository;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.MailSender;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class RegistrationController {
    private final MainService mainService;
    private final UserService userService;

    public RegistrationController(MainService mainService, UserService userService) {
        this.mainService = mainService;
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
        try{
            userService.activateUser(code, model);
            return "login";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }
}
