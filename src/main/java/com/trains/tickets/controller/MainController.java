package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.StationService;
import org.springframework.beans.factory.annotation.Value;
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

    public MainController(StationRepository stationRepository, StationService stationService, MainService mainService) {
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.mainService = mainService;
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
        try{
            mainService.putUserInfoToModel(user, model);
            model.addAttribute("dateNow", LocalDate.now());
            model.addAttribute("stations", stationService.convertAllEntitysToDto(stationRepository.findAll()));
            return "ticketsSearch";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String name,
                      Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            model.addAttribute("dateNow", LocalDate.now());
            return "ticketsSearch";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
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
        try{
            mainService.putUserInfoToModel(user, model);
            model.addAttribute("dateNow", LocalDate.now());
            return "ticketsSearch";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }
}
