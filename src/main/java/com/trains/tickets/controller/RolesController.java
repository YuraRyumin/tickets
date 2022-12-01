package com.trains.tickets.controller;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.RoleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('admin')")
public class RolesController {
    private final RoleRepository roleRepository;

    public RolesController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String rolesList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "roleList";
    }

    @GetMapping("{role}")
    public String roleEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String role,
                               Model model){
        model.addAttribute("role", roleRepository.findById(Integer.parseInt(role)));
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "roleEdit";
    }
    @PostMapping
    public String roleSave(@AuthenticationPrincipal User user,
                           @RequestParam String name,
                           @RequestParam Map<String, String> form,
                           @RequestParam("roleId") Role roleChanged,
                           Model model){
        roleChanged.setName(name);
        roleRepository.save(roleChanged);
        model.addAttribute("user", user);
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/distance";
    }
}
