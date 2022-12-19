package com.trains.tickets.controller;

import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.Stop;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.*;
import com.trains.tickets.graph.Graph;
import com.trains.tickets.graph.GraphService;
import com.trains.tickets.projection.*;
import com.trains.tickets.repository.*;
import com.trains.tickets.service.StopService;
import com.trains.tickets.service.WagonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

@RestController
public class TicketsRestController {
    private final UserRepository userRepository;
    private final StopsForMainDTORepository stopsForMainDTORepository;
    private final WagonRepository wagonRepository;
    private final WagonService wagonService;
    private final StopService stopService;
    private final StopRepository stopRepository;
    private final GraphService graphService;
    private final StationRepository stationRepository;

    public TicketsRestController(UserRepository userRepository, StopsForMainDTORepository stopsForMainDTORepository, WagonRepository wagonRepository, WagonService wagonService, StopService stopService, StopRepository stopRepository, GraphService graphService, StationRepository stationRepository) {
        this.userRepository = userRepository;
        this.stopsForMainDTORepository = stopsForMainDTORepository;
        this.wagonRepository = wagonRepository;
        this.wagonService = wagonService;
        this.stopService = stopService;
        this.stopRepository = stopRepository;
        this.graphService = graphService;
        this.stationRepository = stationRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTableTickets")
    public Set<StopsDirectProjection> getTableTickets(
            @RequestParam String stationFirst,
            @RequestParam String stationLast,
            @RequestParam String dateTicket
            ){
        String[] fullDate = dateTicket.split("-");
        Integer dayOfDate = Integer.valueOf(fullDate[2]);
        Integer monthOfDate = Integer.valueOf(fullDate[1]);
        Integer yearOfDate = Integer.valueOf(fullDate[0]);
        Calendar calendar = new GregorianCalendar(yearOfDate, monthOfDate - 1 , dayOfDate);

        LocalTime timeNow = LocalTime.now();
        timeNow = timeNow.plusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String timeNowPlusTen = formatter.format(timeNow);
        return stopsForMainDTORepository.findDirectScheduleByTwoStations(stationFirst, stationLast, calendar.get(Calendar.DAY_OF_WEEK), timeNowPlusTen);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTableTicketsTransfer")
    public Set<StopsTwoTrainsProjection> getTableTicketsTransfer(
            @RequestParam String stationFirst,
            @RequestParam String stationLast,
            @RequestParam String dateTicket
    ){
        String[] fullDate = dateTicket.split("-");
        Integer dayOfDate = Integer.valueOf(fullDate[2]);
        Integer monthOfDate = Integer.valueOf(fullDate[1]);
        Integer yearOfDate = Integer.valueOf(fullDate[0]);
        Calendar calendar = new GregorianCalendar(yearOfDate, monthOfDate - 1 , dayOfDate);

        LocalTime timeNow = LocalTime.now();
        timeNow = timeNow.plusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String timeNowPlusTen = formatter.format(timeNow);
        return stopsForMainDTORepository.findScheduleByTwoStations(stationFirst, stationLast, calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.DAY_OF_WEEK), timeNowPlusTen);
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
    public Iterable<WagonDTO> getWagons(
            @RequestParam String trainId
    ){
        //return wagonRepository.findByTrainID(Integer.parseInt(trainId));
        return wagonService.convertAllEntityToDto(wagonRepository.findAllByTrainId(Integer.parseInt(trainId)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getSeats")
    public Set<Integer> getSeats(
            @RequestParam String schedule,
            @RequestParam Integer wagonId,
            @RequestParam String dateTicket,
            @RequestParam Integer trainId
    ){
        //return wagonRepository.findSeatsByTrainAndSchedule(schedule, wagonId, dateTicket);
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
        Graph graph = graphService.getFilledGraph();
        Station stationFirst = stationRepository.findByName(strStationFirst);
        Station stationLast = stationRepository.findByName(strStationLast);
        List<Stop> stopList = stopRepository.findAll();
        Set<ManyTrainsTripDTO> twoTrainsDTOS = graph.findWaysBetweenTwoStations(stationFirst, stationLast, stopList, dateTicket);
        graph.clean();
        return twoTrainsDTOS;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getPrice")
    public Float getSeats(@RequestParam String trainNumber,
                          @RequestParam Integer distance,
                          @RequestParam Integer wagonId){
        Float kilometers = wagonService.getPrice(trainNumber, distance, wagonId);
        System.out.println(trainNumber + "; " + distance + "; " + wagonId);
        return kilometers;
    }
}
