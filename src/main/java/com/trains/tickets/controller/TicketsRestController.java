package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.dto.StopsForArrivalsDTO;
import com.trains.tickets.projection.*;
import com.trains.tickets.repository.StopRepository;
import com.trains.tickets.repository.StopsForMainDTORepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.repository.WagonRepository;
import com.trains.tickets.service.StopService;
import com.trains.tickets.service.WagonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class TicketsRestController {
    private final UserRepository userRepository;
    private final StopsForMainDTORepository stopsForMainDTORepository;
    private final WagonRepository wagonRepository;
    private final WagonService wagonService;
    private final StopService stopService;
    private final StopRepository stopRepository;

    public TicketsRestController(UserRepository userRepository, StopsForMainDTORepository stopsForMainDTORepository, WagonRepository wagonRepository, WagonService wagonService, StopService stopService, StopRepository stopRepository) {
        this.userRepository = userRepository;
        this.stopsForMainDTORepository = stopsForMainDTORepository;
        this.wagonRepository = wagonRepository;
        this.wagonService = wagonService;
        this.stopService = stopService;
        this.stopRepository = stopRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTableTickets")
    public Set<StopsDirectProjection> getTableTickets(
            @RequestParam String stationFirst,
            @RequestParam String stationLast
            ){
        return stopsForMainDTORepository.findDirectScheduleByTwoStations(stationFirst, stationLast);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTableTicketsTransfer")
    public Set<StopsTwoTrainsProjection> getTableTicketsTransfer(
            @RequestParam String stationFirst,
            @RequestParam String stationLast
    ){
        return stopsForMainDTORepository.findScheduleByTwoStations(stationFirst, stationLast);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTicketsInfo")
    public Set<TicketInfoProjection> getTicketsInfo(
            @AuthenticationPrincipal User user,
            @RequestParam String stationFirst,
            @RequestParam String stationLast,
            @RequestParam String timeDeparture,
            @RequestParam String timeArrival
    ){
        return stopsForMainDTORepository.findTicketsInfoAndPassanger(user.getPassenger() == null ? null : user.getPassenger().getId(), stationFirst, stationLast, timeDeparture, timeArrival);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getWagons")
    public Set<WagonInfoProjection> getWagons(
            @RequestParam String trainId
    ){
        return wagonRepository.findByTrainID(Integer.parseInt(trainId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getSeats")
    public Set<SeatsProjection> getSeats(
            @RequestParam String schedule,
            @RequestParam Integer wagonId,
            @RequestParam String dateTicket
    ){
        return wagonRepository.findSeatsByTrainAndSchedule(schedule, wagonId, dateTicket);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getArrivalsInfo")
    public Iterable<StopsForArrivalsDTO> getArrivalsInfo(@RequestParam String station){
        return stopService.convertAllEntityToDtoForArrival(stopRepository.findAllByStationName(station));
    }
}
