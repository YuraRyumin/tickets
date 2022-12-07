package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.projection.StopsDirectProjection;
import com.trains.tickets.projection.StopsTwoTrainsProjection;
import com.trains.tickets.projection.TicketInfoProjection;
import com.trains.tickets.repository.StopsForMainDTORepository;
import com.trains.tickets.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
public class TicketsRestController {
    private final UserRepository userRepository;
    private final StopsForMainDTORepository stopsForMainDTORepository;

    public TicketsRestController(UserRepository userRepository, StopsForMainDTORepository stopsForMainDTORepository) {
        this.userRepository = userRepository;
        this.stopsForMainDTORepository = stopsForMainDTORepository;
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
}
