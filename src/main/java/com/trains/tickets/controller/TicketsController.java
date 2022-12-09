package com.trains.tickets.controller;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.*;
import com.trains.tickets.service.*;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
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
    private final UserRepository userRepository;
    private final UserService userService;

    public TicketsController(TicketRepository ticketRepository, TicketService ticketService,
                             PassengerRepository passengerRepository, PassengerService passengerService,
                             TrainRepository trainRepository, TrainService trainService,
                             WagonRepository wagonRepository, WagonService wagonService,
                             ScheduleRepository scheduleRepository, ScheduleService scheduleService, UserRepository userRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.wagonRepository = wagonRepository;
        this.wagonService = wagonService;
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("tickets", ticketService.convertAllEntityToDto(ticketRepository.findAll()));
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));
        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "ticketsList";
    }

    @GetMapping("{ticket}")
    public String userEditForm(@AuthenticationPrincipal User user,
                               @PathVariable String ticket,
                               Model model){
        if (ticket.equals("new")) {
            model.addAttribute("ticket", ticketService.getEmptyDto());
            model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
            model.addAttribute("wagons", wagonService.convertAllEntityToDto(wagonRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
            model.addAttribute("schedules", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
            model.addAttribute("passengers", passengerService.convertAllEntityToDto(passengerRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
            model.addAttribute("users", userService.convertAllEntityToDto(userRepository.findAll(Sort.by(Sort.Direction.ASC, "login"))));
        } else {
            Ticket selectedTicket = ticketRepository.findById(Integer.parseInt(ticket));
            model.addAttribute("ticket", ticketService.convertEntityToDto(selectedTicket));
            model.addAttribute("trains", trainService.convertAllEntityToDtoWithSelected(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number")), selectedTicket.getTrain()));
            model.addAttribute("wagons", wagonService.convertAllEntityToDtoWithSelected(wagonRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedTicket.getWagon()));
            model.addAttribute("schedules", scheduleService.convertAllEntityToDtoWithSelected(scheduleRepository.findAll(), selectedTicket.getSchedule()));
            model.addAttribute("passengers", passengerService.convertAllEntityToDtoWithSelected(passengerRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedTicket.getPassenger()));
            model.addAttribute("users", userService.convertAllEntityToDtoWithSelected(userRepository.findAll(Sort.by(Sort.Direction.ASC, "login")), selectedTicket.getUser()));
        }
        model.addAttribute("user", userService.convertEntityToDtoForNav(user));


        if(user.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(user.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "ticketsEdit";
    }

    @PostMapping
    public String ticketSave(@AuthenticationPrincipal User userThis,
                           @RequestParam String passenger,
                           @RequestParam String dateTicket,
                           @RequestParam String train,
                           @RequestParam String wagon,
                           @RequestParam Integer price,
                           @RequestParam String schedule,
                           @RequestParam Integer ticketId,
                           @RequestParam Integer seat,
                           @RequestParam String user,
                           @RequestParam Map<String, String> form,
                           //@RequestParam("ticketId") Ticket ticketChanged,
                           Model model){
        String[] fullName = passenger.split("\\s");
        String nameOfPassenger = fullName[0];
        String surnameOfPassenger = fullName[1];

        String[] fullDate = dateTicket.split("-");
        Integer dayOfTicket = Integer.valueOf(fullDate[2]);
        Integer monthOfTicket = Integer.valueOf(fullDate[1]);
        Integer yearOfTicket = Integer.valueOf(fullDate[0]);
        LocalDate localDateOfTicket = LocalDate.of(yearOfTicket, monthOfTicket, dayOfTicket);

        User userNew = userRepository.findByLogin(user);
        Passenger passengerNew = passengerRepository.findByNameAndSurname(nameOfPassenger, surnameOfPassenger);
        Train trainNew = trainRepository.findByNumber(train);
        Wagon wagonNew = wagonRepository.findByName(wagon);
        Schedule scheduleNew = scheduleRepository.findByTime(schedule);
        if (ticketId.equals(0)) {
            Ticket ticketChanged = new Ticket(
                 passengerNew,
                 localDateOfTicket,
                 trainNew,
                 wagonNew,
                 price,
                 scheduleNew,
                 seat,
                 userThis
            );
            ticketRepository.save(ticketChanged);
        } else {
            Ticket ticketChanged = ticketRepository.findById(ticketId);
            boolean wasChanged = false;
            if(!ticketChanged.getPassenger().equals(passengerNew)){
                ticketChanged.setPassenger(passengerNew);
                wasChanged = true;
            }
            if(!ticketChanged.getDateTicket().equals(localDateOfTicket)){
                ticketChanged.setDateTicket(localDateOfTicket);
                wasChanged = true;
            }
            if(!ticketChanged.getTrain().equals(trainNew)){
                ticketChanged.setTrain(trainNew);
                wasChanged = true;
            }
            if(!ticketChanged.getWagon().equals(wagonNew)){
                ticketChanged.setWagon(wagonNew);
                wasChanged = true;
            }
            if(!ticketChanged.getPrice().equals(price)){
                ticketChanged.setPrice(price);
                wasChanged = true;
            }
            if(!ticketChanged.getSchedule().equals(scheduleNew)){
                ticketChanged.setSchedule(scheduleNew);
                wasChanged = true;
            }
            if(!ticketChanged.getSeat().equals(seat)){
                ticketChanged.setSeat(seat);
                wasChanged = true;
            }
            if(!ticketChanged.getUser().equals(userNew.getLogin())){
                ticketChanged.setUser(userNew);
                wasChanged = true;
            }
            if(wasChanged){
                ticketRepository.save(ticketChanged);
            }
        }

        model.addAttribute("user", userService.convertEntityToDtoForNav(userThis));
        if(userThis.isAdmin()) {
            model.addAttribute("adminRole", true);
        }
        if(userThis.isOperator()) {
            model.addAttribute("operatorRole", true);
        }
        return "redirect:/tickets";
    }
}
