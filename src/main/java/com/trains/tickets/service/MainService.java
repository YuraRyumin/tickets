package com.trains.tickets.service;

import com.trains.tickets.domain.User;
import com.trains.tickets.dto.ErrorDTO;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class MainService {
    private final UserService userService;

    public MainService(UserService userService) {
        this.userService = userService;
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

    public void putExceptionInfoToModel(Exception e, Model model){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(e.getClass().getName());
        errorDTO.setMessage(e.getMessage());
        errorDTO.setBody(String.valueOf(e.getCause()));
        model.addAttribute("error", errorDTO);
    }
}
