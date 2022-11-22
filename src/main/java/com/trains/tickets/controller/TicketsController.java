package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class TicketsController {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if(filter != null && !filter.isEmpty()) {
            Set<Role> rolesSet = new HashSet<>();
            rolesSet.add(roleRepository.findByName(filter));
            model.put("roles", rolesSet);
        } else {
            Iterable<Role>  rolesI = roleRepository.findAll();
            model.put("roles", rolesI);
        }
        return "main";
    }

    @PostMapping("changePass")
    public String post(Map<String, Object> model){
        //User myUser1 = userRepository.findByLogin("user1");
        //myUser1.setPassword(passwordEncoder.encode(myUser1.getPassword()));
        //userRepository.save(myUser1);
        //User myUser2 = userRepository.findByLogin("user2");
        //myUser2.setPassword(passwordEncoder.encode(myUser2.getPassword()));
        //userRepository.save(myUser2);
        //User myUser3 = userRepository.findByLogin("user3");
        //myUser3.setPassword(passwordEncoder.encode(myUser3.getPassword()));
        //userRepository.save(myUser3);
        //User myUser4 = userRepository.findByLogin("user4");
        //myUser4.setPassword(passwordEncoder.encode(myUser4.getPassword()));
        //userRepository.save(myUser4);
        //User myNewUser = userRepository.findByLogin("newuser");
        //myNewUser.setPassword(passwordEncoder.encode(myNewUser.getPassword()));
        //userRepository.save(myNewUser);
        return "main";
    }
}
