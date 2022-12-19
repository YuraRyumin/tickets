package com.trains.tickets.service;

import com.trains.tickets.domain.*;
import com.trains.tickets.dto.TicketDTO;
import com.trains.tickets.dto.TicketForUserDTO;
import com.trains.tickets.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public TicketService(TicketRepository ticketRepository, PassengerRepository passengerRepository, PassengerService passengerService, TrainRepository trainRepository, TrainService trainService, WagonRepository wagonRepository, WagonService wagonService, ScheduleRepository scheduleRepository, ScheduleService scheduleService, UserRepository userRepository, UserService userService) {
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
    }

    public TicketForUserDTO convertEntityForMainToDto(Ticket ticket){
        TicketForUserDTO ticketForUserDTO = new TicketForUserDTO();

        Passenger passenger = ticket.getPassenger();
        ticketForUserDTO.setName(passenger.getName());
        ticketForUserDTO.setSurname(passenger.getSurname());
        ticketForUserDTO.setPassport(passenger.getPassport());
        ticketForUserDTO.setDateOfBirth(passenger.getDateOfBirth());
        ticketForUserDTO.setGender(passenger.getGender());

        ticketForUserDTO.setDateTicket(ticket.getDateTicket());

        ticketForUserDTO.setTrainNumber(ticket.getTrain().getNumber());
        ticketForUserDTO.setWagonNumber(ticket.getWagon().getName());
        ticketForUserDTO.setSeat(1);
        ticketForUserDTO.setServiceClass(ticket.getWagon().getServiceClasses().getName());
        ticketForUserDTO.setPrice(ticket.getPrice());

        return ticketForUserDTO;
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
        //Schedule scheduleNew = scheduleRepository.findByTime(schedule);
        Schedule scheduleNew = scheduleRepository.findByTimeAndTrainNumber(timeOfSchedule, numberOfTrain);
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
    }
}
