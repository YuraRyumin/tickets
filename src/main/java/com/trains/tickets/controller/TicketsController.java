package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    @Value("${spring.mail.username}")
    private String username;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public TicketsController(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

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
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo("s_shukrik_s@mail.ru");
        mailMessage.setSubject("activation");
        mailMessage.setText("User successfully activated");

        mailSender.send(mailMessage);
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
