package com.trains.tickets.controller;

import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.service.StationService;
import com.trains.tickets.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/stations")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class StationsController {
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final UserService userService;

    public StationsController(StationRepository stationRepository, StationService stationService, UserService userService) {
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.userService = userService;
    }

    @GetMapping
    public String stationList(@AuthenticationPrincipal User user,
                               Model model){
        model.addAttribute("stations", stationService.convertAllEntityToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "stationsList";
    }

    @GetMapping("{station}")
    public String stationEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String station,
                                   Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if (station.equals("new")) {
                model.addAttribute("stations", stationService.getEmptyDto());
            } else {
                model.addAttribute("stations", stationService.convertEntityToDto(stationRepository.findById(Integer.parseInt(station))));
            }
            return "stationsEdit";
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
    public String stationSave(@AuthenticationPrincipal User user,
                               @RequestParam String name,
                               @RequestParam Integer stationId,
                               @RequestParam Map<String, String> form,
                               //@RequestParam("stationId") Station stationChanged,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if(stationId.equals(0)){
                Station stationChanged = new Station(name);
                stationRepository.save(stationChanged);
            } else {
                Station stationChanged = stationRepository.findById(stationId);
                if(!stationChanged.getName().equals(name)){
                    stationChanged.setName(name);
                    stationRepository.save(stationChanged);
                }
            }
            return "redirect:/stations";
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
