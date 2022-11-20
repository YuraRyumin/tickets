package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class TicketsController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "tickets";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model){
        Iterable<Role> role = roleRepository.findAll();
        model.put("roles", role);
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String name,
                      Map<String, Object> model){

        Iterable<Role> roleI = roleRepository.findAll();
        model.put("roles", roleI);
        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter,
                         Map<String, Object> model){
        Iterable<Role> rolesI;
        if(filter != null && !filter.isEmpty()) {
            rolesI = roleRepository.findByName(filter);
        } else {
            rolesI = roleRepository.findAll();
        }
        model.put("roles", rolesI);
        return "main";
    }
}
