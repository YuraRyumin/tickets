package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.dto.*;
import com.trains.tickets.graph.GraphService;
import com.trains.tickets.repository.*;
import com.trains.tickets.service.StopService;
import com.trains.tickets.service.TicketService;
import com.trains.tickets.service.WagonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class TicketsRestController {
    private final WagonRepository wagonRepository;
    private final WagonService wagonService;
    private final StopService stopService;
    private final StopRepository stopRepository;
    private final GraphService graphService;
    private final StationRepository stationRepository;
    private final TicketService ticketService;

    public TicketsRestController(WagonRepository wagonRepository, WagonService wagonService, StopService stopService, StopRepository stopRepository, GraphService graphService, StationRepository stationRepository, TicketService ticketService) {
        this.wagonRepository = wagonRepository;
        this.wagonService = wagonService;
        this.stopService = stopService;
        this.stopRepository = stopRepository;
        this.graphService = graphService;
        this.stationRepository = stationRepository;
        this.ticketService = ticketService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTicketsInfo")
    public Set<TicketInfoDTO> getTicketsInfo(
            @AuthenticationPrincipal User user,
            @RequestParam String stationFirst,
            @RequestParam String stationLast,
            @RequestParam String timeDeparture,
            @RequestParam String timeArrival
    ){
        return ticketService.findTicketsInfoAndPassanger(user, stationFirst, stationLast, timeDeparture, timeArrival);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getWagons")
    public Iterable<WagonDTO> getWagons(
            @RequestParam String trainId
    ){
        return wagonService.convertAllEntityToDto(wagonRepository.findAllByTrainId(Integer.parseInt(trainId)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getSeats")
    public Set<Integer> getSeats(
            @RequestParam String schedule,
            @RequestParam Integer wagonId,
            @RequestParam String dateTicket,
            @RequestParam Integer trainId
    ){
        return wagonService.getSeats(schedule, wagonId, dateTicket, trainId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getArrivalsInfo")
    public Iterable<StopsForArrivalsDTO> getArrivalsInfo(@RequestParam String station){
        return stopService.convertAllEntityToDtoForArrival(stopRepository.findAllByStationName(station));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGraph")
    public Set<ManyTrainsTripDTO> getGraph(@RequestParam String strStationFirst,
                                        @RequestParam String strStationLast,
                                        @RequestParam String dateTicket){
        return graphService.getAllShortestWays(strStationFirst, strStationLast, dateTicket);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getPrice")
    public Float getSeats(@RequestParam String trainNumber,
                          @RequestParam Integer distance,
                          @RequestParam Integer wagonId){
        return wagonService.getPrice(trainNumber, distance, wagonId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getFiltredTicketsTable")
    public Iterable<TicketDTO> getFiltredTicketsTable(@RequestParam String trainTickets,
                                                      @RequestParam String scheduleTickets,
                                                      @RequestParam String dateTickets){
        return ticketService.getFiltredTicketsTable(trainTickets, scheduleTickets, dateTickets);
    }
}
