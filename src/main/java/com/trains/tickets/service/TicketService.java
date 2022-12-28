package com.trains.tickets.service;

import com.trains.tickets.domain.*;
import com.trains.tickets.dto.TicketDTO;
import com.trains.tickets.dto.TicketInfoDTO;
import com.trains.tickets.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Slf4j
@Transactional(readOnly = true)
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
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
    private final StopRepository stopRepository;

    public TicketService(TicketRepository ticketRepository, PassengerRepository passengerRepository, PassengerService passengerService, TrainRepository trainRepository, TrainService trainService, WagonRepository wagonRepository, WagonService wagonService, ScheduleRepository scheduleRepository, ScheduleService scheduleService, UserRepository userRepository, UserService userService, StopRepository stopRepository) {
        this.ticketRepository = ticketRepository;
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.wagonRepository = wagonRepository;
        this.wagonService = wagonService;
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.stopRepository = stopRepository;
    }

    public Iterable<TicketDTO> convertAllEntityToDto(Iterable<Ticket> tickets){
        return StreamSupport.stream(tickets.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public TicketDTO convertEntityToDto(Ticket ticket){
        TicketDTO ticketDTO = new TicketDTO();

        ticketDTO.setId(ticket.getId());
        ticketDTO.setPassenger(ticket.getPassenger().getName() + " " + ticket.getPassenger().getSurname());
        ticketDTO.setDateTicket(ticket.getDateTicket());
        ticketDTO.setTrain(ticket.getTrain().getNumber());
        ticketDTO.setWagon(ticket.getWagon().getName());
        ticketDTO.setPrice(ticket.getPrice());
        ticketDTO.setSchedule(ticket.getSchedule().getTime());
        ticketDTO.setSeat(ticket.getSeat());
        ticketDTO.setUser(ticket.getUser().getLogin());

        return ticketDTO;
    }

    public TicketDTO getEmptyDto(){
        TicketDTO ticketDTO = new TicketDTO();

        ticketDTO.setId(0);
        ticketDTO.setPassenger("");
        ticketDTO.setDateTicket(LocalDate.of(1, 1, 1));
        ticketDTO.setTrain("");
        ticketDTO.setWagon("");
        ticketDTO.setPrice(0);
        ticketDTO.setSchedule("");
        ticketDTO.setSeat(0);
        ticketDTO.setUser("");

        return ticketDTO;
    }

    public void putInfoAboutTicketToModel(String ticket, Model model){
        if (ticket.equals("new")) {
            model.addAttribute("ticket", getEmptyDto());
            model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
            model.addAttribute("wagons", wagonService.convertAllEntityToDto(wagonRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
            model.addAttribute("schedules", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
            model.addAttribute("passengers", passengerService.convertAllEntityToDto(passengerRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
            model.addAttribute("users", userService.convertAllEntityToDto(userRepository.findAll(Sort.by(Sort.Direction.ASC, "login"))));
        } else {
            Ticket selectedTicket = ticketRepository.findById(Integer.parseInt(ticket));
            if(selectedTicket == null){
                throw  new NullPointerException("Ticket not found!");
            }
            model.addAttribute("ticket", convertEntityToDto(selectedTicket));
            model.addAttribute("trains", trainService.convertAllEntityToDtoWithSelected(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number")), selectedTicket.getTrain()));
            model.addAttribute("wagons", wagonService.convertAllEntityToDtoWithSelected(wagonRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedTicket.getWagon()));
            model.addAttribute("schedules", scheduleService.convertAllEntityToDtoWithSelected(scheduleRepository.findAll(), selectedTicket.getSchedule()));
            model.addAttribute("passengers", passengerService.convertAllEntityToDtoWithSelected(passengerRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedTicket.getPassenger()));
            model.addAttribute("users", userService.convertAllEntityToDtoWithSelected(userRepository.findAll(Sort.by(Sort.Direction.ASC, "login")), selectedTicket.getUser()));
        }
    }

    @Transactional
    public void saveTicket(String passenger,
                           String dateTicket,
                           String train,
                           String wagon,
                           Integer price,
                           String schedule,
                           Integer ticketId,
                           Integer seat,
                           String user,
                           User userThis){
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
        String[] fullNameSchedule = schedule.split("_->_");
        String numberOfTrain = fullNameSchedule[0];
        String timeOfSchedule = fullNameSchedule[1];
        Schedule scheduleNew = scheduleRepository.findByTimeAndTrainNumber(timeOfSchedule, numberOfTrain);
        if (ticketId.equals(0)) {
            if(trainNew != null && userNew != null && passengerNew != null && wagonNew != null && scheduleNew != null) {
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
                log.info(LocalDateTime.now().toString() + " - " + userThis.getLogin() + " create new ticket with id " +
                        ticketChanged.getId() + " (" +
                        ticketChanged.getPassenger().getName() + " " + ticketChanged.getPassenger().getSurname() + "; " +
                        ticketChanged.getUser().getLogin() + "; " +
                        ticketChanged.getTrain().getNumber() + "; " +
                        ticketChanged.getSchedule().getTime().toString() + "; " +
                        ticketChanged.getDateTicket().toString() + "; " +
                        ticketChanged.getWagon().getName() + "; " +
                        ticketChanged.getSeat().toString() + "; " +
                        ticketChanged.getPrice().toString() + ")");
            }
        } else {
            Ticket ticketChanged = ticketRepository.findById(ticketId);
            boolean wasChanged = false;
            if(!ticketChanged.getPassenger().equals(passengerNew) && passengerNew != null){
                ticketChanged.setPassenger(passengerNew);
                wasChanged = true;
            }
            if(!ticketChanged.getDateTicket().equals(localDateOfTicket)){
                ticketChanged.setDateTicket(localDateOfTicket);
                wasChanged = true;
            }
            if(!ticketChanged.getTrain().equals(trainNew) && trainNew != null){
                ticketChanged.setTrain(trainNew);
                wasChanged = true;
            }
            if(!ticketChanged.getWagon().equals(wagonNew) && wagonNew != null){
                ticketChanged.setWagon(wagonNew);
                wasChanged = true;
            }
            if(!ticketChanged.getPrice().equals(price)){
                ticketChanged.setPrice(price);
                wasChanged = true;
            }
            if(!ticketChanged.getSchedule().equals(scheduleNew) && scheduleNew != null){
                ticketChanged.setSchedule(scheduleNew);
                wasChanged = true;
            }
            if(!ticketChanged.getSeat().equals(seat)){
                ticketChanged.setSeat(seat);
                wasChanged = true;
            }
            if(userNew != null) {
                if (!ticketChanged.getUser().equals(userNew.getLogin())) {
                    ticketChanged.setUser(userNew);
                    wasChanged = true;
                }
            }
            if(wasChanged){
                ticketRepository.save(ticketChanged);
                log.info(LocalDateTime.now().toString() + " - " + userThis.getLogin() + " change ticket with id " +
                        ticketChanged.getId() + " (" +
                        ticketChanged.getPassenger().getName() + " " + ticketChanged.getPassenger().getSurname() + "; " +
                        ticketChanged.getUser().getLogin() + "; " +
                        ticketChanged.getTrain().getNumber() + "; " +
                        ticketChanged.getSchedule().getTime().toString() + "; " +
                        ticketChanged.getDateTicket().toString() + "; " +
                        ticketChanged.getWagon().getName() + "; " +
                        ticketChanged.getSeat().toString() + "; " +
                        ticketChanged.getPrice().toString() + ")");
            }
        }
    }

    public Iterable<TicketDTO> getFiltredTicketsTable(String trainTickets,
                                                      String scheduleTickets,
                                                      String dateTickets){
        if(trainTickets.isEmpty() && scheduleTickets.isEmpty() && dateTickets.isEmpty()){
            return convertAllEntityToDto(ticketRepository.findAll());
        }

        String[] fullDate = dateTickets.split("-");
        Integer dayOfTicket = Integer.valueOf(fullDate[2]);
        Integer monthOfTicket = Integer.valueOf(fullDate[1]);
        Integer yearOfTicket = Integer.valueOf(fullDate[0]);
        LocalDate localDateOfTickets = LocalDate.of(yearOfTicket, monthOfTicket, dayOfTicket);

        Train trainEntity = trainRepository.findByNumber(trainTickets);

        String[] fullName = scheduleTickets.split("_->_");
        String numberOfTrain = fullName[0];
        String timeOfSchedule = fullName[1];
        Schedule scheduleEntity = scheduleRepository.findByTimeAndTrainNumber(timeOfSchedule, numberOfTrain);

        Set<Ticket> ticketList = ticketRepository.findAllByDateTicketAndTrainAndSchedule(localDateOfTickets, trainEntity, scheduleEntity);

        return convertAllEntityToDto(ticketList);
    }

    public Set<TicketInfoDTO> findTicketsInfoAndPassanger(User user,
                                                          String firstStation,
                                                          String lastStation,
                                                          String timeDeparture,
                                                          String timeArrival){
        Passenger passengerTicket = user.getPassenger();
        Integer idPassenger = null;
        if(passengerTicket != null){
            idPassenger = passengerTicket.getId();
        }
        Set<TicketInfoDTO> ticketInfoDTOS = new HashSet<>();
        TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();

        String[] fullTimeDeparture = timeDeparture.split(":");
        Integer hourOfDeparture = Integer.valueOf(fullTimeDeparture[0]);
        Integer minuteOfDeparture = Integer.valueOf(fullTimeDeparture[1]);
        LocalTime localTimeDeparture = LocalTime.of(hourOfDeparture, minuteOfDeparture, 0);

        String[] fullTimeArrival = timeArrival.split(":");
        Integer hourOfArrival = Integer.valueOf(fullTimeArrival[0]);
        Integer minuteOfArrival = Integer.valueOf(fullTimeArrival[1]);
        LocalTime localTimeArrival = LocalTime.of(hourOfArrival, minuteOfArrival, 0);

        if(idPassenger != null){
            Passenger passenger = passengerRepository.findById(idPassenger);
            ticketInfoDTO.setPassengerName(passenger.getName());
            ticketInfoDTO.setPassengerSurname(passenger.getSurname());
            ticketInfoDTO.setPassengerDate(passenger.getDateOfBirth().toString());
            ticketInfoDTO.setPassengerGender(passenger.getGender());
            ticketInfoDTO.setPassengerPassport(passenger.getPassport());
        } else {
            ticketInfoDTO.setPassengerName("");
            ticketInfoDTO.setPassengerSurname("");
            ticketInfoDTO.setPassengerDate("");
            ticketInfoDTO.setPassengerGender("");
            ticketInfoDTO.setPassengerPassport("");
        }

        Set<Stop> stopsFirst = stopRepository.findAllByStationNameAndTimeEnd(firstStation, localTimeDeparture);
        Set<Stop> stopsSecond = stopRepository.findAllByStationNameAndTimeBegining(lastStation, localTimeArrival);

        Schedule schedule = null;
        Stop stopFirst = null;
        Stop stopLast = null;

        for(Stop stopFirstCicle: stopsFirst){
            for(Stop stopSecondCicle: stopsSecond){
                if(stopFirstCicle.getSchedule() != null &&
                        stopSecondCicle.getSchedule() != null &&
                        stopFirstCicle.getSchedule().equals(stopSecondCicle.getSchedule())){
                    schedule = stopFirstCicle.getSchedule();
                    stopFirst = stopFirstCicle;
                    stopLast = stopSecondCicle;
                }
            }
        }

        if(schedule != null) {
            ticketInfoDTO.setSchedName(schedule.getTrain().getNumber() + "_->_" + schedule.getTime().toString());
            ticketInfoDTO.setScheduleID(schedule.getId());
            ticketInfoDTO.setSchedule(schedule.getTime().toString());
            ticketInfoDTO.setTrainId(schedule.getTrain().getId().toString());
            ticketInfoDTO.setTrainNumber(schedule.getTrain().getNumber());
        } else {
            ticketInfoDTO.setSchedName("");
            ticketInfoDTO.setScheduleID(0);
            ticketInfoDTO.setSchedule("");
            ticketInfoDTO.setTrainId("");
            ticketInfoDTO.setTrainNumber("");
        }
        if(stopFirst != null) {
            ticketInfoDTO.setStationFirst(stopFirst.getStation().getName());
            ticketInfoDTO.setTimeDeparture(stopFirst.getTimeEnd().toString());
        } else {
            ticketInfoDTO.setStationFirst("");
            ticketInfoDTO.setTimeDeparture("");
        }
        if(stopLast != null){
            ticketInfoDTO.setStationLast(stopLast.getStation().getName());
            ticketInfoDTO.setTimeArrival(stopLast.getTimeBegining().toString());
        } else {
            ticketInfoDTO.setStationLast("");
            ticketInfoDTO.setStationLast("");
        }

        ticketInfoDTOS.add(ticketInfoDTO);
        return ticketInfoDTOS;
    }
}
