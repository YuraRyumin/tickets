package com.trains.tickets.service;

import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Service
public class MainService {
    private final UserService userService;
    private final StationService stationService;
    private final StationRepository stationRepository;
    private final PassengerService passengerService;

    public MainService(UserService userService, StationService stationService, StationRepository stationRepository, PassengerService passengerService) {
        this.userService = userService;
        this.stationService = stationService;
        this.stationRepository = stationRepository;
        this.passengerService = passengerService;
    }

    public void putUserInfoToModel(User user, Model model){
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        if(user != null) {
            if (user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if (user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
        }
    }

    public void putMainInfoToModel(User user, Model model){
        model.addAttribute("dateNow", LocalDate.now());
        model.addAttribute("stations", stationService.convertAllEntitysToDto(stationRepository.findAll()));
        if(user.getPassenger() == null){
            model.addAttribute("passenger", passengerService.getEmptyDto());
        }else {
            model.addAttribute("passenger", passengerService.convertEntityToDto(user.getPassenger()));
        }
        model.addAttribute("genders", passengerService.getGendersList(user.getPassenger()));
    }

    public void putExceptionInfoToModel(Exception e, Model model){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(e.getClass().getName());
        errorDTO.setMessage(e.getMessage());
        errorDTO.setBody(String.valueOf(e.getCause()));
        model.addAttribute("error", errorDTO);
    }
}
