package com.trains.tickets.controller;

import com.trains.tickets.domain.ServiceClass;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import com.trains.tickets.repository.ServiceClassRepository;
import com.trains.tickets.service.ServiceClassService;
import com.trains.tickets.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/serviceClasses")
@PreAuthorize("hasAuthority('operator')")
//@PreAuthorize("hasAuthority('admin')")
public class ServiceClassesController {
    private final ServiceClassRepository serviceClassRepository;
    private final ServiceClassService serviceClassService;
    private final UserService userService;

    public ServiceClassesController(ServiceClassRepository serviceClassRepository, ServiceClassService serviceClassService, UserService userService) {
        this.serviceClassRepository = serviceClassRepository;
        this.serviceClassService = serviceClassService;
        this.userService = userService;
    }

    @GetMapping
    public String serviceClassList(@AuthenticationPrincipal User user,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            model.addAttribute("serviceClasses", serviceClassService.convertAllEntityToDto(serviceClassRepository.findAll()));
            return "serviceClassesList";
        } catch (Exception e){
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(e.getClass().getName());
            errorDTO.setMessage(e.getMessage());
            errorDTO.setBody(String.valueOf(e.getCause()));
            model.addAttribute("error", errorDTO);
            return "error";
        }
    }

    @GetMapping("{serviceClass}")
    public String serviceClassEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String serviceClass,
                                   Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if (serviceClass.equals("new")) {
                model.addAttribute("serviceClass", serviceClassService.getEmptyDto());
            } else {
                model.addAttribute("serviceClass", serviceClassService.convertEntityToDto(serviceClassRepository.findById(Integer.parseInt(serviceClass))));
            }
            return "serviceClassesEdit";
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
    public String serviceClassSave(@AuthenticationPrincipal User user,
                               @RequestParam String name,
                               @RequestParam Float prisePerKm,
                               @RequestParam Integer serviceClassId,
                               @RequestParam Map<String, String> form,
                               //@RequestParam("serviceClassId") ServiceClass serviceClassChanged,
                               Model model){
        try{
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if(user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if(user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
            if (serviceClassId.equals(0)) {
                ServiceClass serviceClassChanged = new ServiceClass(
                        name,
                        prisePerKm
                );
                serviceClassRepository.save(serviceClassChanged);
            } else {
                ServiceClass serviceClassChanged = serviceClassRepository.findById(serviceClassId);
                boolean wasChanged = false;
                if(!serviceClassChanged.getName().equals(name)){
                    serviceClassChanged.setName(name);
                    wasChanged = true;
                }
                if(!serviceClassChanged.getPrisePerKm().equals(prisePerKm)){
                    serviceClassChanged.setPrisePerKm(prisePerKm);
                    wasChanged = true;
                }
                if(wasChanged){
                    serviceClassRepository.save(serviceClassChanged);
                }
            }
            return "redirect:/serviceClasses";
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
