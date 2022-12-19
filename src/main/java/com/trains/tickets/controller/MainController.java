package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.PassengerService;
import com.trains.tickets.service.StationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class MainController {
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final MainService mainService;
    private final PassengerService passengerService;

    public MainController(StationRepository stationRepository, StationService stationService, MainService mainService, PassengerService passengerService) {
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.mainService = mainService;
        this.passengerService = passengerService;
    }

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user,
                           Model model) {
        mainService.putUserInfoToModel(user, model);
        return "tickets";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       Model model){
        mainService.putUserInfoToModel(user, model);
        mainService.putMainInfoToModel(user, model);

        return "ticketsSearch";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String name,
                      Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("dateNow", LocalDate.now());
        return "ticketsSearch";
    }

    @PostMapping("/issueTicket")
    public String issueTicket(@AuthenticationPrincipal User user,
            @RequestParam String name,
            Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("dateNow", LocalDate.now());
    return "issueTicket";
}

    @PostMapping("changePass")
    public String post(@AuthenticationPrincipal User user,
                       Model model){
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
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("dateNow", LocalDate.now());
        return "ticketsSearch";
    }
}
