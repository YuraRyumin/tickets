package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.*;
import com.trains.tickets.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/takeTicket")
public class TakeTicketController {
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final PassengerRepository passengerRepository;
    private final PassengerService passengerService;
    private final TrainRepository trainRepository;
    private final TrainService trainService;
    private final WagonRepository wagonRepository;
    private final WagonService wagonService;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;

    public TakeTicketController(TicketRepository ticketRepository, TicketService ticketService, PassengerRepository passengerRepository, PassengerService passengerService, TrainRepository trainRepository, TrainService trainService, WagonRepository wagonRepository, WagonService wagonService, ScheduleRepository scheduleRepository, ScheduleService scheduleService) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.wagonRepository = wagonRepository;
        this.wagonService = wagonService;
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public String takeTicket(@AuthenticationPrincipal User user,
                             @RequestParam String stationFirstFirstTicket,
                             @RequestParam String timeDepartureFirstTicket,
                             @RequestParam String stationLastFirstTicket,
                             @RequestParam String timeArrivalFirstTicket,
                             @RequestParam String passengerNameFirstTicket,
                             @RequestParam String passengerSurnameFirstTicket,
                             @RequestParam String passengerGenderFirstTicket,
                             @RequestParam Map<String, String> form,
                             //@RequestParam("trainId") Train trainChanged,
                             Model model){
        System.out.println(stationFirstFirstTicket + "; " +
                timeDepartureFirstTicket + "; " +
                stationLastFirstTicket + "; " +
                timeArrivalFirstTicket + "; " +
                passengerNameFirstTicket + "; " +
                passengerSurnameFirstTicket + "; " +
                passengerGenderFirstTicket);
        return "redirect:/ticketsSearch";
    }

//    @PostMapping
//    public String takeTicket(@AuthenticationPrincipal User user,
//                             @RequestParam String stationFirstFirstTicket,
//                             @RequestParam String timeDepartureFirstTicket,
//                             @RequestParam String stationLastFirstTicket,
//                             @RequestParam String timeArrivalFirstTicket,
//                             @RequestParam String passengerNameFirstTicket,
//                             @RequestParam String passengerSurnameFirstTicket,
//                             @RequestParam String passengerGenderFirstTicket,
//                            @RequestParam String stationFirstSecondTicket,
//                            @RequestParam String timeDepartureSecondTicket,
//                            @RequestParam String stationLastSecondTicket,
//                            @RequestParam String timeArrivalSecondTicket,
//                            @RequestParam String passengerNameSecondTicket,
//                            @RequestParam String passengerSurnameSecondTicket,
//                            @RequestParam String passengerGenderSecondTicket,
//                            @RequestParam Map<String, String> form,
//                            //@RequestParam("trainId") Train trainChanged,
//                            Model model){
//        System.out.println(stationFirstFirstTicket + "; " +
//                timeDepartureFirstTicket + "; " +
//                stationLastFirstTicket + "; " +
//                timeArrivalFirstTicket + "; " +
//                passengerNameFirstTicket + "; " +
//                passengerSurnameFirstTicket + "; " +
//                passengerGenderFirstTicket + "; " +
//                stationFirstSecondTicket + "; " +
//                timeDepartureSecondTicket + "; " +
//                stationLastSecondTicket + "; " +
//                timeArrivalSecondTicket + "; " +
//                passengerNameSecondTicket + "; " +
//                passengerSurnameSecondTicket + "; " +
//                passengerGenderSecondTicket);
//        return "redirect:/ticketsSearch";
//    }
}
