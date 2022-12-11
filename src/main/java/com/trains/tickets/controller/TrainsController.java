package com.trains.tickets.controller;

import com.trains.tickets.domain.Train;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.TrainRepository;
import com.trains.tickets.service.TrainService;
import com.trains.tickets.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/trains")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class TrainsController {
    private final TrainRepository trainRepository;
    private final TrainService trainService;
    private final UserService userService;

    public TrainsController(TrainRepository trainRepository, TrainService trainService, UserService userService) {
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.userService = userService;
    }

    @GetMapping
    public String trainList(@AuthenticationPrincipal User user,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll()));
            return "trainsList";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.addAttribute("error", errorDTO);
            return "error";
        }
    }

    @GetMapping("{train}")
    public String trainEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String train,
                                   Model model){
        try {
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if (train.equals("new")) {
                model.addAttribute("train", trainService.getEmptyDto());
            } else {
                model.addAttribute("train", trainService.convertEntityToDto(trainRepository.findById(Integer.parseInt(train))));
            }
            return "trainsEdit";
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
    public String trainSave(@AuthenticationPrincipal User user,
                                @RequestParam String number,
                                @RequestParam Integer seats,
                                @RequestParam Integer trainId,
                                @RequestParam Map<String, String> form,
                                //@RequestParam("trainId") Train trainChanged,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if (trainId.equals(0)) {
                Train trainChanged = new Train(
                    number,
                    seats
                );
                trainRepository.save(trainChanged);
            } else {
                Train trainChanged = trainRepository.findById(trainId);
                if (trainChanged == null){
                    throw  new NullPointerException("Train not found!");
                }
                boolean wasChanged = false;
                if(!trainChanged.getNumber().equals(number)){
                    trainChanged.setNumber(number);
                    wasChanged = true;
                }
                if(!trainChanged.getSeats().equals(seats)){
                    trainChanged.setSeats(seats);
                    wasChanged = true;
                }
                if(wasChanged){
                    trainRepository.save(trainChanged);
                }
            }
            return "redirect:/trains";
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
