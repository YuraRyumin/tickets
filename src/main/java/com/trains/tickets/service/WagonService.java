package com.trains.tickets.service;

import com.trains.tickets.domain.*;
import com.trains.tickets.dto.WagonDTO;
import com.trains.tickets.repository.ServiceClassRepository;
import com.trains.tickets.repository.TicketRepository;
import com.trains.tickets.repository.TrainRepository;
import com.trains.tickets.repository.WagonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Slf4j
@Transactional(readOnly = true)
@Service
public class WagonService {
    private final TrainRepository trainRepository;
    private final TrainService trainService;
    private final ServiceClassRepository serviceClassRepository;
    private final ServiceClassService serviceClassService;
    private final WagonRepository wagonRepository;
    private final TicketRepository ticketRepository;

    public WagonService(TrainRepository trainRepository, TrainService trainService, ServiceClassRepository serviceClassRepository, ServiceClassService serviceClassService, WagonRepository wagonRepository, TicketRepository ticketRepository) {
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.serviceClassRepository = serviceClassRepository;
        this.serviceClassService = serviceClassService;
        this.wagonRepository = wagonRepository;
        this.ticketRepository = ticketRepository;
    }

    public Iterable<WagonDTO> convertAllEntityToDto(Iterable<Wagon> wagons){
        return StreamSupport.stream(wagons.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Iterable<WagonDTO> convertAllEntityToDtoWithSelected(Iterable<Wagon> wagons, Wagon selectedWagon){
        return StreamSupport.stream(wagons.spliterator(), false)
                .map(wagon -> {
                    WagonDTO wagonDTO = convertEntityToDto(wagon);
                    if (wagon.getName().equals(selectedWagon.getName())){
                        wagonDTO.setSelected(true);
                    } else {
                        wagonDTO.setSelected(false);
                    }
                    return wagonDTO;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public WagonDTO convertEntityToDto(Wagon wagon){
        WagonDTO wagonDTO = new WagonDTO();

        wagonDTO.setId(wagon.getId());
        wagonDTO.setTrain(wagon.getTrain().getNumber());
        wagonDTO.setSeats(wagon.getSeats());
        wagonDTO.setServiceClasses(wagon.getServiceClasses().getName());
        wagonDTO.setName(wagon.getName());

        return wagonDTO;
    }

    public WagonDTO getEmptyDto(){
        WagonDTO wagonDTO = new WagonDTO();

        wagonDTO.setId(0);
        wagonDTO.setTrain("");
        wagonDTO.setSeats(0);
        wagonDTO.setServiceClasses("");
        wagonDTO.setName("");

        return wagonDTO;
    }

    public void putInfoAboutWagonToModel(String wagon, Model model){
        if (wagon.equals("new")) {
            model.addAttribute("wagon", getEmptyDto());
            model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
            model.addAttribute("serviceClasses", serviceClassService.convertAllEntityToDto(serviceClassRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        } else {
            Wagon selectedWagon = wagonRepository.findById(Integer.parseInt(wagon));
            if(selectedWagon == null){
                throw  new NullPointerException("Wagon not found!");
            }
            model.addAttribute("wagon", convertEntityToDto(selectedWagon));
            model.addAttribute("trains", trainService.convertAllEntityToDtoWithSelected(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number")), selectedWagon.getTrain()));
            model.addAttribute("serviceClasses", serviceClassService.convertAllEntityToDtoWithSelected(serviceClassRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedWagon.getServiceClasses()));
        }
    }

    @Transactional
    public void saveWagon(String train, String serviceClasses, String name, Integer seats, Integer wagonId, User user){
        ServiceClass serviceClassNew = serviceClassRepository.findByName(serviceClasses);
        Train trainNew = trainRepository.findByNumber(train);
        if(wagonId.equals(0)){
            Wagon wagonChanged = new Wagon(
                    trainNew,
                    serviceClassNew,
                    name,
                    seats
            );
            wagonRepository.save(wagonChanged);
            log.error(LocalDateTime.now().toString() + " - " + user.getLogin() + " create new wagon with id " +
                    wagonChanged.getId() + " (" +
                    wagonChanged.getName() + "; " +
                    wagonChanged.getTrain().getNumber() + "; " +
                    wagonChanged.getSeats().toString() + "; " +
                    wagonChanged.getServiceClasses().getName() + ")");
        } else {
            Wagon wagonChanged = wagonRepository.findById(wagonId);
            if(wagonChanged == null){
                throw  new NullPointerException("Wagon not found!");
            }
            boolean wasChanged = false;
            if(!wagonChanged.getTrain().equals(trainNew)){
                wagonChanged.setTrain(trainNew);
                wasChanged = true;
            }
            if(!wagonChanged.getServiceClasses().equals(serviceClassNew)){
                wagonChanged.setServiceClasses(serviceClassNew);
                wasChanged = true;
            }
            if(!wagonChanged.getName().equals(name)){
                wagonChanged.setName(name);
                wasChanged = true;
            }
            if(!wagonChanged.getSeats().equals(seats)){
                wagonChanged.setSeats(seats);
                wasChanged = true;
            }
            if(wasChanged){
                wagonRepository.save(wagonChanged);
                log.error(LocalDateTime.now().toString() + " - " + user.getLogin() + " change wagon with id " +
                        wagonChanged.getId() + " (" +
                        wagonChanged.getName() + "; " +
                        wagonChanged.getTrain().getNumber() + "; " +
                        wagonChanged.getSeats().toString() + "; " +
                        wagonChanged.getServiceClasses().getName() + ")");
            }
        }
    }

    public Float getPrice(String trainNumber, Integer distance, Integer wagonId){
        Float pricePerKm = Float.valueOf(0);
        if(!wagonId.equals(null) && !wagonId.equals(0)){
            Wagon wagonN = wagonRepository.findById(wagonId);
            if(wagonN != null){
                ServiceClass serviceClass = wagonN.getServiceClasses();
                if(!serviceClass.equals(null)) {
                    pricePerKm = serviceClass.getPrisePerKm();
                }
            }
        } else {
            Set<Wagon> wagons = wagonRepository.findAllByTrainNumber(trainNumber);
            for (Wagon wagon : wagons) {
                if (wagon.getId() != null) {
                    ServiceClass serviceClass = wagon.getServiceClasses();
                    if (!serviceClass.equals(null)) {
                        pricePerKm = serviceClass.getPrisePerKm();
                    }
                    break;
                }
            }
        }
        return pricePerKm * distance;
    }

    public Set<Integer> getSeats(String schedule, Integer wagonId, String dateTicket, Integer trainId){
        Set<Wagon> wagons = wagonRepository.findAllByTrainId(trainId);
        for (Wagon wagon: wagons){
            if(wagon.getId() != null){
                wagonId = wagon.getId();
                break;
            }
        }
        Set<Integer> freeSeats = new HashSet<>();
        if (wagonId == null){
            return freeSeats;
        }
        Wagon selectedWagon = wagonRepository.findById(wagonId);
        if(selectedWagon == null){
            return freeSeats;
        }
        String[] fullDate = dateTicket.split("-");
        Integer dayOfTicket = Integer.valueOf(fullDate[2]);
        Integer monthOfTicket = Integer.valueOf(fullDate[1]);
        Integer yearOfTicket = Integer.valueOf(fullDate[0]);
        Set<Ticket> tickets = ticketRepository.findAllByDateTicketAndScheduleTimeAndWagonId(LocalDate.of(yearOfTicket, monthOfTicket, dayOfTicket), schedule, wagonId);
        boolean wasSaled = false;
        for(Integer i = 1; i <= selectedWagon.getSeats(); i++) {
            wasSaled = false;
            for (Ticket ticket : tickets) {
                if(i.equals(ticket.getSeat())){
                    wasSaled = true;
                }
            }
            if(!wasSaled){
                freeSeats.add(i);
            }
        }
        return freeSeats;
    }
}
