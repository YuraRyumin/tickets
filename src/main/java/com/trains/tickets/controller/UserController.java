package com.trains.tickets.controller;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.PassengerRepository;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.MailSender;
import com.trains.tickets.service.PassengerService;
import com.trains.tickets.service.RoleService;
import com.trains.tickets.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

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
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    public UserController(UserRepository userRepository, UserService userService, RoleRepository roleRepository, RoleService roleService, PassengerRepository passengerRepository, PassengerService passengerService, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("users", userService.convertAllEntityToDto(userRepository.findAll()));
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
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
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));

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
            @RequestParam String email,
            @RequestParam String telephone,
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String activationCode,
            @RequestParam String passenger,
            @RequestParam String role,
            @RequestParam Integer userId,
            @RequestParam Map<String, String> form,
            //@RequestParam("userId") User userChanged,
            Model model){
        String[] fullName = passenger.split("\\s");
        String nameOfPassenger = fullName[0];
        String surnameOfPassenger = fullName[1];
        Passenger passengerNew = passengerRepository.findByNameAndSurname(nameOfPassenger, surnameOfPassenger);
        Role roleNew = roleRepository.findByName(role);
        if (userId.equals(0)) {
            User userChanged = new User(
                email,
                telephone,
                login,
                passwordEncoder.encode(password),
                passengerNew,
                roleNew,
                true,
                activationCode,
                UUID.randomUUID().toString()
            );
            if(user.getEmail() != ""){
                String message = String.format(
                        "Hello, %s! \n" +
                                "Welcom to Trains. Please visit next link: http://localhost:8080/activate/%s",
                        user.getLogin(), user.getActivationCode()
                );
                mailSender.send(user.getEmail(), "Activation", message);
            }
            userRepository.save(userChanged);
        } else {
            User userChanged = userRepository.findById(userId);
            boolean wasChanged = false;
            if(!userChanged.getEmail().equals(email)){
                userChanged.setEmail(email);
                wasChanged = true;
            }
            if(!userChanged.getTelephone().equals(telephone)){
                userChanged.setTelephone(telephone);
                wasChanged = true;
            }
            if(!userChanged.getLogin().equals(login)){
                userChanged.setLogin(login);
                wasChanged = true;
            }
//            if(!userChanged.getPassenger().equals(passwordEncoder.encode(password))){
//                userChanged.setPassword(passwordEncoder.encode(password));
//                wasChanged = true;
//            }
            if (userChanged.getUuid().equals("") || userChanged.getUuid() == null){
                userChanged.setUuid(UUID.randomUUID().toString());
                wasChanged = true;
            }
//            if(!userChanged.getPassenger() == null || !passengerNew == null) {
//                if (!userChanged.getPassenger().equals(passengerNew)) {
//                    userChanged.setPassenger(passengerNew);
//                    wasChanged = true;
//                }
//            }
            if(!userChanged.getRole().equals(roleNew)){
                userChanged.setRole(roleNew);
                wasChanged = true;
            }
            userChanged.setActive(true);
//            if(!userChanged.getActivationCode().equals(activationCode)){
//                userChanged.setActivationCode(activationCode);
//                wasChanged = true;
//            }
            if(wasChanged){
                userRepository.save(userChanged);
            }
        }

        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/user";
    }
}
