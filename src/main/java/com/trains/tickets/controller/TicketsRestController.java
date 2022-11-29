package com.trains.tickets.controller;

import com.trains.tickets.projection.StopsForMainProjection;
import com.trains.tickets.projection.StopsTwoTrainsProjection;
import com.trains.tickets.repository.StopsForMainDTORepository;
import com.trains.tickets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class TicketsRestController {
    @Value("${spring.datasource.url}")
    private String urlSql;
    @Value("${spring.datasource.username}")
    private String usernameSql;
    @Value("${spring.datasource.password}")
    private String passwordSql;

    private final UserRepository userRepository;
    private final StopsForMainDTORepository stopsForMainDTORepository;

    public TicketsRestController(UserRepository userRepository, StopsForMainDTORepository stopsForMainDTORepository) {
        this.userRepository = userRepository;
        this.stopsForMainDTORepository = stopsForMainDTORepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTableTickets")
    public Set<StopsForMainProjection> getTableTickets(
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
        Set<StopsTwoTrainsProjection> srtStops = stopsForMainDTORepository.findScheduleByTwoStations(stationFirst, stationLast);
        return stopsForMainDTORepository.findScheduleByTwoStations(stationFirst, stationLast);
    }
}
