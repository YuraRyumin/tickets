package com.trains.tickets.controller;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.PassengerRepository;
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

    public PassengersController(PassengerRepository passengerRepository, PassengerService passengerService, UserService userService) {
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
        this.userService = userService;
    }

    @GetMapping
    public String PassengersList(@AuthenticationPrincipal User user,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            model.addAttribute("passengers", passengerService.convertAllEntityToDto(passengerRepository.findAll()));
            return "passengersList";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.addAttribute("error", errorDTO);
            return "error";
        }
    }

    @GetMapping("{passenger}")
    public String PassengerEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String passenger,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if (passenger.equals("new")) {
                model.addAttribute("passenger", passengerService.getEmptyDto());
            } else {
                model.addAttribute("passenger", passengerService.convertEntityToDto(passengerRepository.findById(Integer.parseInt(passenger))));
            }
            return "passengersEdit";
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
    public String userSave(@AuthenticationPrincipal User user,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String passport,
                           @RequestParam String gender,
                           @RequestParam String dateOfBirth,
                           @RequestParam Integer passengerId,
                           @RequestParam Map<String, String> form,
                           //@RequestParam("passengerId") Passenger passengerChanged,
                           Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            String[] fullDate = dateOfBirth.split("-");
            Integer dayOfBirth = Integer.valueOf(fullDate[2]);
            Integer monthOfBirth = Integer.valueOf(fullDate[1]);
            Integer yearOfBirth = Integer.valueOf(fullDate[0]);
            LocalDate localDateOfBirth = LocalDate.of(yearOfBirth,monthOfBirth,dayOfBirth);
            System.out.println(dateOfBirth);
            if (passengerId.equals(0)) {
                Passenger passengerChanged = new Passenger(
                        name,
                        surname,
                        passport,
                        gender,
                        localDateOfBirth
                );
                passengerRepository.save(passengerChanged);
            } else {
                Passenger passengerChanged = passengerRepository.findById(passengerId);
                boolean wasChanged = false;
                if(!passengerChanged.getName().equals(name)){
                    passengerChanged.setName(name);
                    wasChanged = true;
                }
                if(!passengerChanged.getSurname().equals(surname)){
                    passengerChanged.setSurname(surname);
                    wasChanged = true;
                }
                if(!passengerChanged.getPassport().equals(passport)){
                    passengerChanged.setPassport(passport);
                    wasChanged = true;
                }
                if(!passengerChanged.getGender().equals(gender)){
                    passengerChanged.setGender(gender);
                    wasChanged = true;
                }
                if(!passengerChanged.getDateOfBirth().equals(localDateOfBirth)){
                    passengerChanged.setDateOfBirth(localDateOfBirth);
                    wasChanged = true;
                }
                if(wasChanged){
                    passengerRepository.save(passengerChanged);
                }
            }
            return "redirect:/passengers";
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
