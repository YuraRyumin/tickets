package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable String user, Model model){
        model.addAttribute("user", userRepository.findById(Integer.parseInt(user)));
        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String login,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user){
        user.setLogin(login);
        userRepository.save(user);
        return "redirect:/user";
    }
}
