package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.TrainRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.TrainService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/trains")
@PreAuthorize("hasAuthority('operator')")
public class TrainsController {
    private final TrainRepository trainRepository;
    private final TrainService trainService;
    private final MainService mainService;

    public TrainsController(TrainRepository trainRepository, TrainService trainService, MainService mainService) {
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.mainService = mainService;
    }

    @GetMapping
    public String trainList(@AuthenticationPrincipal User user,
                               Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll()));
        return "trainsList";
    }

    @GetMapping("{train}")
    public String trainEditForm(@AuthenticationPrincipal User user,
                                   @PathVariable String train,
                                   Model model){
        mainService.putUserInfoToModel(user, model);
        trainService.putInfoAboutTrainToModel(train, model);
        return "trainsEdit";
    }
    @PostMapping
    public String trainSave(@AuthenticationPrincipal User user,
                                @RequestParam String number,
                                @RequestParam Integer trainId,
                                Model model){
        mainService.putUserInfoToModel(user, model);
        trainService.saveTrain(number, trainId, user);
        return "redirect:/trains";
    }
}
