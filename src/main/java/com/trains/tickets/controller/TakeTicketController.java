package com.trains.tickets.controller;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.*;
import com.trains.tickets.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private final StationRepository stationRepository;
    private final StationService stationService;

    public TakeTicketController(TicketRepository ticketRepository, TicketService ticketService, PassengerRepository passengerRepository, PassengerService passengerService, TrainRepository trainRepository, TrainService trainService, WagonRepository wagonRepository, WagonService wagonService, ScheduleRepository scheduleRepository, ScheduleService scheduleService, StationRepository stationRepository, StationService stationService) {
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
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    @PostMapping
    public String takeTicket(@AuthenticationPrincipal User user,
                             //@RequestParam String dateTicket,
                             @RequestParam Map<String, String> form,
                             Map<String, Object> model){
        LocalDate localDateTicket = null;
        if (form.containsKey("dateTicket")) {
            String dateTicket = form.get("dateTicket");
            System.out.println(dateTicket);
            String[] fullTimeTicket = dateTicket.split("-");
            Integer dayOfTicket = Integer.valueOf(fullTimeTicket[2]);
            Integer monthOfTicket = Integer.valueOf(fullTimeTicket[1]);
            Integer yearOfTicket = Integer.valueOf(fullTimeTicket[0]);
            localDateTicket = LocalDate.of(yearOfTicket, monthOfTicket, dayOfTicket);
        }
        System.out.println(localDateTicket);
// First ticket info
        Station stationFirstFirstTicket = null;
        if (form.containsKey("stationFirstFirstTicket")) {
            stationFirstFirstTicket = stationRepository.findByName(form.get("stationFirstFirstTicket").replace("_", " "));
        }
        System.out.println(stationFirstFirstTicket);
        String timeDepartureFirstTicket = null;
    //    LocalTime localTimeDepartureFirstTicket = null;
        if (form.containsKey("timeDepartureFirstTicket")) {
            timeDepartureFirstTicket = form.get("timeDepartureFirstTicket");
        }
        Station stationLastFirstTicket = null;
        if (form.containsKey("stationLastFirstTicket")) {
            stationLastFirstTicket = stationRepository.findByName(form.get("stationLastFirstTicket").replace("_", " "));
        }
        System.out.println(stationLastFirstTicket);
        String timeArrivalFirstTicket = null;
    //    LocalTime localTimeArrivalFirstTicket = null;
        if (form.containsKey("timeArrivalFirstTicket")) {
            timeArrivalFirstTicket = form.get("timeArrivalFirstTicket");
        }
        Integer seatFirstTicket = null;
        if (form.containsKey("seatsFirstTicket")) {
            seatFirstTicket = Integer.valueOf(form.get("seatsFirstTicket"));
        }
        Wagon wagonFirstTicket = null;
        if (form.containsKey("wagonFirstTicket")) {
            wagonFirstTicket = wagonRepository.findByName(form.get("wagonFirstTicket"));
        }
        Train trainFirstTicket = null;
        if (form.containsKey("trainFirstTicket")) {
            trainFirstTicket = trainRepository.findByNumber(form.get("trainFirstTicket"));
        }
        Schedule scheduleFirstTicket = null;
        if (form.containsKey("scheduleFirstTicket")) {
            scheduleFirstTicket = scheduleRepository.findByTime(form.get("scheduleFirstTicket"));
        }
        System.out.println(scheduleFirstTicket);
// Second ticket info
        Station stationFirstSecondTicket = null;
        if (form.containsKey("stationFirstSecondTicket")) {
            stationFirstSecondTicket = stationRepository.findByName(form.get("stationFirstSecondTicket"));
        }
        String timeDepartureSecondTicket = null;
    //    LocalTime localTimeDepartureSecondTicket = null;
        if (form.containsKey("timeDepartureSecondTicket")) {
            timeDepartureSecondTicket = form.get("timeDepartureSecondTicket");
        }
        Station stationLastSecondTicket = null;
        if (form.containsKey("stationLastSecondTicket")) {
            stationLastSecondTicket = stationRepository.findByName(form.get("stationLastSecondTicket"));
        }
        String timeArrivalSecondTicket = null;
    //    LocalTime localTimeArrivalSecondTicket = null;
        if (form.containsKey("timeArrivalSecondTicket")) {
            timeArrivalSecondTicket = form.get("timeArrivalSecondTicket");
        }
        Integer seatSecondTicket = null;
        if (form.containsKey("seatsSecondTicket")) {
            seatSecondTicket = Integer.valueOf(form.get("seatsSecondTicket"));
        }
        Wagon wagonSecondTicket = null;
        if (form.containsKey("wagonSecondTicket")) {
            wagonSecondTicket = wagonRepository.findByName(form.get("wagonSecondTicket"));
        }
        Train trainSecondTicket = null;
        if (form.containsKey("trainSecondTicket")) {
            trainSecondTicket = trainRepository.findByNumber(form.get("trainSecondTicket"));
        }
        Schedule scheduleSecondTicket = null;
        if (form.containsKey("scheduleSecondTicket")) {
            scheduleSecondTicket = scheduleRepository.findByTime(form.get("scheduleSecondTicket"));
        }

// Passenger info
        Passenger passengerForTicket = null;
        if (form.containsKey("passengerName")) {
            passengerForTicket = passengerRepository.findByNameAndSurname(form.get("passengerName"), form.get("passengerSurname"));
        }
//        String passengerNameFirstTicket = null;
//        if (form.containsKey("passengerSurnameSecondTicket")) {
//            passengerNameFirstTicket = form.get("passengerSurnameSecondTicket");
//        }
//        String passengerSurnameFirstTicket = null;
//        if (form.containsKey("passengerSurnameSecondTicket")) {
//            passengerSurnameFirstTicket = form.get("passengerSurnameSecondTicket");
//        }

        String passengerGender = null;
        if (form.containsKey("passengerGender")) {
            passengerGender = form.get("passengerGender");
        }
//        correctPassanger(passengerFirstTicket, passengerNameFirstTicket,
//                passengerSurnameFirstTicket, passengerGenderFirstTicket);



        System.out.println(stationFirstFirstTicket + "; " +
                timeDepartureFirstTicket + "; " +
                stationLastFirstTicket + "; " +
                timeArrivalFirstTicket + "; " +
                seatFirstTicket + "; " +
                stationFirstSecondTicket + "; " +
                timeDepartureSecondTicket + "; " +
                form.get("stationLastFirstTicket") + "; " +
                stationLastSecondTicket + "; " +
                timeArrivalSecondTicket + "; " +
                seatSecondTicket + "; " +
                passengerForTicket + "; " +
                form.get("stationFirstFirstTicket").replace("&nbsp;", " ") + "; " +
                localDateTicket + "; " +
                passengerGender);
        LocalDate testDate = LocalDate.of(2022, 5, 1);
        System.out.println(form.get("ticket_table"));

        if(!stationFirstFirstTicket.equals(null) && !stationLastFirstTicket.equals(null)){
            createTicket(passengerForTicket,
                    localDateTicket,
                    trainFirstTicket,
                    wagonFirstTicket,
                    0,
                    scheduleFirstTicket,
                    seatFirstTicket,
                    user);
        }
        model.put("dateNow", LocalDate.now());
        return "redirect:/ticketsSearch";
    }

    private void correctPassanger(Passenger passenger, String passengerName, String passengerSurname, String passengerGender){
        boolean wasChange = false;
        if(!passenger.getName().equals(passengerName)){
            passenger.setName(passengerName);
            wasChange = true;
        }
        if(!passenger.getSurname().equals(passengerSurname)){
            passenger.setSurname(passengerSurname);
            wasChange = true;
        }
        if(!passenger.getGender().equals(passengerGender)){
            passenger.setGender(passengerGender);
            wasChange = true;
        }
        if(wasChange){
            passengerRepository.save(passenger);
        }
    }

    private void createTicket(Passenger passenger,
                              LocalDate dateTicket,
                              Train train,
                              Wagon wagon,
                              Integer price,
                              Schedule schedule,
                              Integer seat,
                              User user){
        Ticket ticket = new Ticket(passenger, dateTicket, train, wagon, price, schedule, seat, user);
        ticketRepository.save(ticket);
    }
}
