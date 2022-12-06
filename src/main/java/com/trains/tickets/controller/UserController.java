package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('admin')")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("users", userService.convertAllEntityToDto(userRepository.findAll()));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "userList";
    }

    @GetMapping("{userThis}")
    public String userEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String userThis,
                               Model model){
        if (userThis.equals("new")) {
            model.addAttribute("userThis", userService.getEmptyDto());
        } else {
            model.addAttribute("userThis", userService.convertEntityToDto(userRepository.findById(Integer.parseInt(userThis))));
        }
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "userEdit";
    }

    @PostMapping
    public String userSave(@AuthenticationPrincipal User user,
            @RequestParam String login,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User userChanged,
            Model model){
        userChanged.setLogin(login);
        userRepository.save(userChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/user";
    }
}
