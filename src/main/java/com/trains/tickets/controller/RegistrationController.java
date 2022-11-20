package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userFromDB = userRepository.findByLogin(user.getLogin());
        if(userFromDB != null){
            model.put("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setId_passenger(1);
        user.setId_role(2);
        userRepository.save(user);
        return "redirect:/login";
    }
}
