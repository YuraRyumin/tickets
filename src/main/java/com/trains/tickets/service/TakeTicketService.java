package com.trains.tickets.service;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
@Slf4j
@Transactional(readOnly = true)
@Service
public class TakeTicketService {
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final TrainRepository trainRepository;
    private final WagonRepository wagonRepository;
    private final ScheduleRepository scheduleRepository;
    private final StationRepository stationRepository;
    private final UserService userService;

    public TakeTicketService(TicketRepository ticketRepository, PassengerRepository passengerRepository, TrainRepository trainRepository, WagonRepository wagonRepository, ScheduleRepository scheduleRepository, StationRepository stationRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.passengerRepository = passengerRepository;
        this.trainRepository = trainRepository;
        this.wagonRepository = wagonRepository;
        this.scheduleRepository = scheduleRepository;
        this.stationRepository = stationRepository;
        this.userService = userService;
    }

    @Transactional
    public void saveTicket(User user, Map<String, String> form, Model model){
        LocalDate localDateTicket = null;
        if (form.containsKey("dateTicket")) {
            String dateTicket = form.get("dateTicket");
            String[] fullTimeTicket = dateTicket.split("-");
            Integer dayOfTicket = Integer.valueOf(fullTimeTicket[2]);
            Integer monthOfTicket = Integer.valueOf(fullTimeTicket[1]);
            Integer yearOfTicket = Integer.valueOf(fullTimeTicket[0]);
            localDateTicket = LocalDate.of(yearOfTicket, monthOfTicket, dayOfTicket);
        }
        // Passenger info
        Passenger passengerForTicket = null;
        if (form.containsKey("passengerName")) {
            passengerForTicket = passengerRepository.findByNameAndSurname(form.get("passengerName"), form.get("passengerSurname"));
        }

        if(passengerForTicket == null) {
            String passengerNameForTicket = null;
            if (form.containsKey("passengerName")) {
                passengerNameForTicket = form.get("passengerName");
            }
            String passengerSurnameForTicket = null;
            if (form.containsKey("passengerSurname")) {
                passengerSurnameForTicket = form.get("passengerSurname");
            }
            String passengerPassportForTicket = null;
            if (form.containsKey("passengerPassport")) {
                passengerPassportForTicket = form.get("passengerPassport");
            }
            String passengerGenderForTicket = null;
            if (form.containsKey("passengerGender")) {
                passengerGenderForTicket = form.get("passengerGender");
            }

            String passengerDateForTicket = null;
            LocalDate localDateOfBirth = null;
            if (form.containsKey("passengerDate")) {
                passengerDateForTicket = form.get("passengerDate");
                if(passengerDateForTicket != null) {
                    String[] fullDate = passengerDateForTicket.split("-");
                    Integer dayOfBirth = Integer.valueOf(fullDate[2]);
                    Integer monthOfBirth = Integer.valueOf(fullDate[1]);
                    Integer yearOfBirth = Integer.valueOf(fullDate[0]);
                    localDateOfBirth = LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth);
                }
            }
            passengerForTicket = new Passenger(passengerNameForTicket,
                    passengerSurnameForTicket,
                    passengerPassportForTicket,
                    passengerGenderForTicket,
                    localDateOfBirth);
            passengerRepository.save(passengerForTicket);
            log.info(LocalDateTime.now().toString() + " - " + user.getLogin() + " create new passenger with id " +
                    passengerForTicket.getId() + " (" +
                    passengerForTicket.getName() + "; " +
                    passengerForTicket.getSurname() + "; " +
                    passengerForTicket.getGender() + "; " +
                    passengerForTicket.getDateOfBirth().toString() + "; " +
                    passengerForTicket.getPassport() + ")");
        }

        for(Integer i = 0; i < 10; i++){
            // ticket info
            Station stationFirstTicket = null;
            if (form.containsKey("stationFirstN" + i)) {
                stationFirstTicket = stationRepository.findByName(form.get("stationFirstN" + i).replace("_", " "));
            }
            String timeDepartureTicket = null;
            LocalTime localTimeDepartureTicket = null;
            if (form.containsKey("timeDepartureN" + i)) {
                timeDepartureTicket = form.get("timeDepartureN" + i);
                if(timeDepartureTicket != null){
                    String[] fullTimeBegining = timeDepartureTicket.split(":");
                    Integer hourOfBegining = Integer.valueOf(fullTimeBegining[0]);
                    Integer minuteOfBegining = Integer.valueOf(fullTimeBegining[1]);
                    localTimeDepartureTicket = LocalTime.of(hourOfBegining, minuteOfBegining, 0);
                }
            }
            Station stationLastTicket = null;
            if (form.containsKey("stationLastN" + i)) {
                stationLastTicket = stationRepository.findByName(form.get("stationLastN" + i).replace("_", " "));
            }
            if(stationFirstTicket == null && stationLastTicket == null){
                continue;
            }
            String timeArrivalTicket = null;
            LocalTime localTimeArrivalTicket = null;
            if (form.containsKey("timeArrivalN" + i)) {
                timeArrivalTicket = form.get("timeArrivalN" + i);
            }
            Integer seatTicket = null;
            if (form.containsKey("seatsN" + i)) {
                seatTicket = Integer.valueOf(form.get("seatsN" + i));
            }
            Wagon wagonTicket = null;
            if (form.containsKey("wagonN" + i)) {
                wagonTicket = wagonRepository.findById(Integer.valueOf(form.get("wagonN" + i)));
            }
            Train trainTicket = null;
            if (form.containsKey("trainN" + i)) {
                trainTicket = trainRepository.findByNumber(form.get("trainN" + i));
            }
            Integer priceTicket = null;
            if (form.containsKey("priceN" + i)) {
                priceTicket = Integer.valueOf(form.get("priceN" + i));
            }
            Schedule scheduleTicket = null;
            if (form.containsKey("scheduleN" + i)) {
                String[] fullName = form.get("scheduleN" + i).split("_->_");
                String numberOfTrain = fullName[0];
                String timeOfSchedule = fullName[1];
                //scheduleFirstTicket = scheduleRepository.findByTime(form.get("scheduleFirstTicket"));
                scheduleTicket = scheduleRepository.findByTimeAndTrainNumber(timeOfSchedule, numberOfTrain);
            }
            if(checkPassengerInTrain(passengerForTicket, localDateTicket, trainTicket, scheduleTicket)){
                model.addAttribute("message", "Passenger is alredy registred for this train!");
                return;
            }
            if(checkSeatInTrain(seatTicket, localDateTicket, trainTicket, scheduleTicket)){
                model.addAttribute("message", "Sorry, this place is already taken.");
                return;
            }
            if(localDateTicket.equals(LocalDate.now()) && checkTimeOfTicket(localTimeDepartureTicket)){
                model.addAttribute("message", "Sorry, before the departure of train there is at less 10 minutes.");
                return;
            }

            if(stationFirstTicket != null && stationLastTicket != null){
                createTicket(passengerForTicket,
                        localDateTicket,
                        trainTicket,
                        wagonTicket,
                        priceTicket,
                        scheduleTicket,
                        seatTicket,
                        user);
            }
        }
    }

    private boolean checkPassengerInTrain(Passenger passenger,
                                          LocalDate localDate,
                                          Train train,
                                          Schedule schedule){
        Ticket ticket = ticketRepository.findFirstByPassengerAndDateTicketAndTrainAndSchedule(passenger, localDate, train, schedule);
        if(ticket == null){
            return false;
        }
        return true;
    }

    private boolean checkSeatInTrain(Integer seat,
                                          LocalDate localDate,
                                          Train train,
                                          Schedule schedule){
        Ticket ticket = ticketRepository.findBySeatAndDateTicketAndTrainAndSchedule(seat, localDate, train, schedule);
        if(ticket == null){
            return false;
        }
        return true;
    }

    private boolean checkTimeOfTicket(LocalTime timeOfTicket){
        LocalTime localTimeNow = LocalTime.now();
        localTimeNow = localTimeNow.plusMinutes(10);
        if(localTimeNow.isAfter(timeOfTicket)){
            return true;
        }
        return false;
    }

    @Transactional
    public void correctPassanger(Passenger passenger, String passengerName, String passengerSurname, String passengerGender, User user){
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
            log.info(LocalDateTime.now().toString() + " - " + user.getLogin() + " change passenger with id " +
                    passenger.getId() + " (" +
                    passenger.getName() + "; " +
                    passenger.getSurname() + "; " +
                    passenger.getGender() + "; " +
                    passenger.getDateOfBirth().toString() + "; " +
                    passenger.getPassport() + ")");
        }
    }

    @Transactional
    public void createTicket(Passenger passenger,
                              LocalDate dateTicket,
                              Train train,
                              Wagon wagon,
                              Integer price,
                              Schedule schedule,
                              Integer seat,
                              User user){
        Ticket ticket = new Ticket(passenger, dateTicket, train, wagon, price, schedule, seat, user);
        ticketRepository.save(ticket);
        log.info(LocalDateTime.now().toString() + " - " + user.getLogin() + " create new ticket with id " +
                ticket.getId() + " (" +
                ticket.getPassenger().getName() + " " + ticket.getPassenger().getSurname() + "; " +
                ticket.getUser().getLogin() + "; " +
                ticket.getTrain().getNumber() + "; " +
                ticket.getSchedule().getTime().toString() + "; " +
                ticket.getDateTicket().toString() + "; " +
                ticket.getWagon().getName() + "; " +
                ticket.getSeat().toString() + "; " +
                ticket.getPrice().toString() + ")");
    }
}
