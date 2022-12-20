package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('admin')")
public class RolesController {
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final MainService mainService;

    public RolesController(RoleRepository roleRepository, RoleService roleService, MainService mainService) {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
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
        roleService.putInfoAboutRoleToModel(model, role);
        return "roleEdit";
    }
    @PostMapping
    public String roleSave(@AuthenticationPrincipal User user,
                           @RequestParam String name,
                           @RequestParam Integer roleId,
                           Model model){
        mainService.putUserInfoToModel(user, model);
        roleService.saveRole(name, roleId, user);
        return "redirect:/roles";
    }
}
