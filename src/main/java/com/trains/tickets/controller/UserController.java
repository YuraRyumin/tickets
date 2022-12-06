package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.PassengerRepository;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.PassengerService;
import com.trains.tickets.service.RoleService;
import com.trains.tickets.service.UserService;
import org.springframework.data.domain.Sort;
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
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final PassengerRepository passengerRepository;
    private final PassengerService passengerService;

    public UserController(UserRepository userRepository, UserService userService, RoleRepository roleRepository, RoleService roleService, PassengerRepository passengerRepository, PassengerService passengerService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
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
            model.addAttribute("roles", roleService.convertAllEntityToDto(roleRepository.findAll()));
            model.addAttribute("passengers", passengerService.convertAllEntityToDto(passengerRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        } else {
            User selectedUser = userRepository.findById(Integer.parseInt(userThis));
            model.addAttribute("userThis", userService.convertEntityToDto(selectedUser));
            model.addAttribute("roles", roleService.convertAllEntityToDtoWithSelected(roleRepository.findAll(), selectedUser.getRole()));
            model.addAttribute("passengers", passengerService.convertAllEntityToDtoWithSelected(passengerRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedUser.getPassenger()));
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
