package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.RoleService;
import com.trains.tickets.service.UserService;
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
    private final RoleService roleService;
    private final UserService userService;
    private final MainService mainService;

    public RolesController(RoleRepository roleRepository, RoleService roleService, UserService userService, MainService mainService) {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.userService = userService;
        this.mainService = mainService;
    }

    @GetMapping
    public String rolesList(@AuthenticationPrincipal User user,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("roles", roleService.convertAllEntityToDto(roleRepository.findAll()));
        return "roleList";
    }

    @GetMapping("{role}")
    public String roleEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String role,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        if (role.equals("new")) {
            model.addAttribute("role", roleService.getEmptyDto());
        } else {
            model.addAttribute("role", roleService.convertEntityToDto(roleRepository.findById(Integer.parseInt(role))));
        }
        return "roleEdit";
    }
    @PostMapping
    public String roleSave(@AuthenticationPrincipal User user,
                           @RequestParam String name,
                           @RequestParam Integer roleId,
                           @RequestParam Map<String, String> form,
                           //@RequestParam("roleId") Role roleChanged,
                           Model model){
        mainService.putUserInfoToModel(user, model);
        roleService.saveRole(name, roleId);
        return "redirect:/roles";
    }
}
