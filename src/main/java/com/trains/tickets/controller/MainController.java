package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.NewsRepository;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.NewsService;
import com.trains.tickets.service.StationService;
import com.trains.tickets.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

@Controller
public class MainController {
    @Value("${spring.mail.username}")
    private String username;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final StationService stationService;
    private final UserService userService;
    private final NewsRepository newsRepository;
    private final NewsService newsService;

    public MainController(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, StationRepository stationRepository, StationService stationService, UserService userService, NewsRepository newsRepository, NewsService newsService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.userService = userService;
        this.newsRepository = newsRepository;
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user,
                           Map<String, Object> model) {
        model.put("user", userService.convertEntityToDtoForNav(user));
        if(user != null) {
            if (user.isAdmin()) {
                model.put("adminRole", true);
            }
            if (user.isOperator()) {
                model.put("operatorRole", true);
            }
        }
        return "tickets";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       Map<String, Object> model){
        try{
            model.put("user", userService.convertEntityToDtoForNav(user));
            model.put("dateNow", LocalDate.now());
            if(user.isAdmin()) {
                model.put("adminRole", true);
            }
            if(user.isOperator()) {
                model.put("operatorRole", true);
            }
            model.put("stations", stationService.convertAllEntitysToDto(stationRepository.findAll()));
            return "ticketsSearch";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.put("error", errorDTO);
            return "error";
        }
    }

//    @GetMapping("/news")
//    public String news(@AuthenticationPrincipal User user,
//                       Map<String, Object> model){
//        try{
//            if(user != null) {
//                model.put("user", userService.convertEntityToDtoForNav(user));
//                if (user.isAdmin()) {
//                    model.put("adminRole", true);
//                }
//                if (user.isOperator()) {
//                    model.put("operatorRole", true);
//                }
//            }
//            model.put("news", newsService.convertAllEntityToDto(newsRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))));
//            return "news";
//        } catch (Exception e){
//            ErrorDTO errorDTO = new ErrorDTO();
//            errorDTO.setCode(e.getClass().getName());
//            errorDTO.setMessage(e.getMessage());
//            errorDTO.setBody(String.valueOf(e.getCause()));
//            model.put("error", errorDTO);
//            return "error";
//        }
//    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String name,
                      Map<String, Object> model){
        try{
            model.put("user", userService.convertEntityToDtoForNav(user));
            model.put("dateNow", LocalDate.now());
            if(user.isAdmin()) {
                model.put("adminRole", true);
            }
            if(user.isOperator()) {
                model.put("operatorRole", true);
            }
            return "ticketsSearch";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.put("error", errorDTO);
            return "error";
        }
    }



    @PostMapping("changePass")
    public String post(@AuthenticationPrincipal User user,
                       Map<String, Object> model){
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        mailMessage.setFrom(username);
//        mailMessage.setTo("s_shukrik_s@mail.ru");
//        mailMessage.setSubject("activation");
//        mailMessage.setText("User successfully activated");
//
//        mailSender.send(mailMessage);
//        model.put("user", user);
        //return "main";
        try{
            model.put("user", userService.convertEntityToDtoForNav(user));
            model.put("dateNow", LocalDate.now());
            if(user.isAdmin()) {
                model.put("adminRole", true);
            }
            if(user.isOperator()) {
                model.put("operatorRole", true);
            }
            return "ticketsSearch";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.put("error", errorDTO);
            return "error";
        }
    }
}
