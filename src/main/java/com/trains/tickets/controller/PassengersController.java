package com.trains.tickets.controller;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.PassengerRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.PassengerService;
import com.trains.tickets.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/passengers")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class PassengersController {
    private final PassengerRepository passengerRepository;
    private final PassengerService passengerService;
    private final UserService userService;
    private final MainService mainService;

    public PassengersController(PassengerRepository passengerRepository, PassengerService passengerService, UserService userService, MainService mainService) {
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
        this.userService = userService;
        this.mainService = mainService;
    }

    @GetMapping
    public String PassengersList(@AuthenticationPrincipal User user,
                               Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            model.addAttribute("passengers", passengerService.convertAllEntityToDto(passengerRepository.findAll()));
            return "passengersList";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }

    @GetMapping("{passenger}")
    public String PassengerEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String passenger,
                               Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            passengerService.putInfoAboutPassengerToModel(model, passenger);
            return "passengersEdit";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }
    @PostMapping
    public String userSave(@AuthenticationPrincipal User user,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String passport,
                           @RequestParam String gender,
                           @RequestParam String dateOfBirth,
                           @RequestParam Integer passengerId,
                           Model model){
        try{
            mainService.putUserInfoToModel(user, model);
            passengerService.savePassenger(name, surname, passport, gender, dateOfBirth, passengerId);
            return "redirect:/passengers";
        } catch (Exception e){
            mainService.putExceptionInfoToModel(e, model);
            return "error";
        }
    }
}
