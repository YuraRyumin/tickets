package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('admin')")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final MainService mainService;

    public UserController(UserRepository userRepository, UserService userService, MainService mainService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mainService = mainService;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        try{
            model.addAttribute("users", userService.convertAllEntityToDto(userRepository.findAll()));
            mainService.putUserInfoToModel(user, model);
            return "userList";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }

    @GetMapping("{userThis}")
    public String userEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String userThis,
                               Model model){
        try {
            mainService.putUserInfoToModel(user, model);
            userService.putInfoAboutUserToModel(userThis, model);
            return "userEdit";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }

    @PostMapping
    public String userSave(@AuthenticationPrincipal User user,
            @RequestParam String email,
            @RequestParam String telephone,
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String activationCode,
            @RequestParam String passenger,
            @RequestParam String role,
            @RequestParam Integer userId,
            Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            userService.saveUser(email, telephone, login, password, activationCode, passenger, role, userId);
            return "redirect:/user";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }
}
