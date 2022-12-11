package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.RoleRepository;
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

    public RolesController(RoleRepository roleRepository, RoleService roleService, UserService userService) {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public String rolesList(@AuthenticationPrincipal User user,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            model.addAttribute("roles", roleService.convertAllEntityToDto(roleRepository.findAll()));
            return "roleList";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.addAttribute("error", errorDTO);
            return "error";
        }
    }

    @GetMapping("{role}")
    public String roleEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String role,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if (role.equals("new")) {
                model.addAttribute("role", roleService.getEmptyDto());
            } else {
                model.addAttribute("role", roleService.convertEntityToDto(roleRepository.findById(Integer.parseInt(role))));
            }
            return "roleEdit";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.addAttribute("error", errorDTO);
            return "error";
        }
    }
    @PostMapping
    public String roleSave(@AuthenticationPrincipal User user,
                           @RequestParam String name,
                           @RequestParam Integer roleId,
                           @RequestParam Map<String, String> form,
                           //@RequestParam("roleId") Role roleChanged,
                           Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if (roleId.equals(0)) {
                Role roleChanged = new Role(name);
                roleRepository.save(roleChanged);
            } else {
                Role roleChanged = roleRepository.findById(roleId);
                if(!roleChanged.getName().equals(name)){
                    roleChanged.setName(name);
                    roleRepository.save(roleChanged);
                }
            }
            return "redirect:/roles";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.addAttribute("error", errorDTO);
            return "error";
        }
    }
}
