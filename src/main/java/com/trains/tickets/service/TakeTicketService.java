package com.trains.tickets.service;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Map;

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

    public void saveTicket(User user, Map<String, String> form, Model model){
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
                if(!passengerDateForTicket.equals(null)) {
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
        }

        for(Integer i = 0; i < 10; i++){
            // First ticket info
            Station stationFirstFirstTicket = null;
            if (form.containsKey("stationFirstN" + i)) {
                stationFirstFirstTicket = stationRepository.findByName(form.get("stationFirstN" + i).replace("_", " "));
            }
            System.out.println(stationFirstFirstTicket);
            String timeDepartureFirstTicket = null;
            //    LocalTime localTimeDepartureFirstTicket = null;
            if (form.containsKey("timeDepartureN" + i)) {
                timeDepartureFirstTicket = form.get("timeDepartureN" + i);
            }
            Station stationLastFirstTicket = null;
            if (form.containsKey("stationLastN" + i)) {
                stationLastFirstTicket = stationRepository.findByName(form.get("stationLastN" + i).replace("_", " "));
            }
            System.out.println(stationLastFirstTicket);
            if(stationFirstFirstTicket == null && stationLastFirstTicket == null){
                continue;
            }
            String timeArrivalFirstTicket = null;
            //    LocalTime localTimeArrivalTicket = null;
            if (form.containsKey("timeArrivalN" + i)) {
                timeArrivalFirstTicket = form.get("timeArrivalN" + i);
            }
            Integer seatFirstTicket = null;
            if (form.containsKey("seatsN" + i)) {
                seatFirstTicket = Integer.valueOf(form.get("seatsN" + i));
            }
            Wagon wagonFirstTicket = null;
            if (form.containsKey("wagonN" + i)) {
                wagonFirstTicket = wagonRepository.findById(Integer.valueOf(form.get("wagonN" + i)));
            }
            Train trainFirstTicket = null;
            if (form.containsKey("trainN" + i)) {
                trainFirstTicket = trainRepository.findByNumber(form.get("trainN" + i));
            }
            Schedule scheduleFirstTicket = null;
            if (form.containsKey("scheduleN" + i)) {
                String[] fullName = form.get("scheduleN" + i).split("_->_");
                String numberOfTrain = fullName[0];
                String timeOfSchedule = fullName[1];
                //scheduleFirstTicket = scheduleRepository.findByTime(form.get("scheduleFirstTicket"));
                scheduleFirstTicket = scheduleRepository.findByTimeAndTrainNumber(timeOfSchedule, numberOfTrain);
            }
            System.out.println(scheduleFirstTicket);

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
        }
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
