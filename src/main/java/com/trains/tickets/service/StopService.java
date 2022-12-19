package com.trains.tickets.service;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.Stop;
import com.trains.tickets.dto.StopDTO;
import com.trains.tickets.dto.StopsForArrivalsDTO;
import com.trains.tickets.repository.ScheduleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.StopRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StopService {
    private final ScheduleRepository scheduleRepository;
    private final StopRepository stopRepository;
    private final ScheduleService scheduleService;
    private final StationRepository stationRepository;
    private final StationService stationService;

    public StopService(ScheduleRepository scheduleRepository, StopRepository stopRepository, ScheduleService scheduleService, StationRepository stationRepository, StationService stationService) {
        this.scheduleRepository = scheduleRepository;
        this.stopRepository = stopRepository;
        this.scheduleService = scheduleService;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    public Iterable<StopDTO> convertAllEntityToDto(Iterable<Stop> stops){
        return StreamSupport.stream(stops.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public StopDTO convertEntityToDto(Stop stop){
        StopDTO stopDTO = new StopDTO();

        stopDTO.setId(stop.getId());
        stopDTO.setStation(stop.getStation().getName());
        stopDTO.setSchedule(stop.getSchedule().getTime());
        stopDTO.setTimeBegining(stop.getTimeBegining());
        stopDTO.setTimeEnd(stop.getTimeEnd());

        return stopDTO;
    }

    public Iterable<StopsForArrivalsDTO> convertAllEntityToDtoForArrival(Iterable<Stop> stops){
        return StreamSupport.stream(stops.spliterator(), false)
                .map(this::convertEntityToDtoForArrival)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public StopsForArrivalsDTO convertEntityToDtoForArrival(Stop stop){
        StopsForArrivalsDTO stopsForArrivalsDTO = new StopsForArrivalsDTO();

        stopsForArrivalsDTO.setStation(stop.getStation().getName());
        stopsForArrivalsDTO.setSchedule(stop.getSchedule().getTime());
        stopsForArrivalsDTO.setTimeBegining(stop.getTimeBegining());
        stopsForArrivalsDTO.setTimeEnd(stop.getTimeEnd());
        stopsForArrivalsDTO.setTrain(stop.getSchedule().getTrain().getNumber());

        return stopsForArrivalsDTO;
    }

    public StopDTO getEmptyDto(){
        StopDTO stopDTO = new StopDTO();

        stopDTO.setId(0);
        stopDTO.setStation("");
        stopDTO.setSchedule("");
        stopDTO.setTimeBegining(LocalTime.of(1, 1, 1));
        stopDTO.setTimeEnd(LocalTime.of(1, 1, 1));

        return stopDTO;
    }

    public void putInfoAboutStopToModel(String stop, Model model){
        if (stop.equals("new")) {
            model.addAttribute("stop", getEmptyDto());
            model.addAttribute("stations", stationService.convertAllEntityToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
            model.addAttribute("schedule", scheduleService.convertAllEntityToDto(scheduleRepository.findAll()));
        } else {
            Stop selectedStop = stopRepository.findById(Integer.parseInt(stop));
            if(selectedStop == null){
                throw  new NullPointerException("Stop not found!");
            }
            model.addAttribute("stop", convertEntityToDto(selectedStop));
            model.addAttribute("stations", stationService.convertAllEntityToDtoWithSelected(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedStop.getStation()));
            model.addAttribute("schedule", scheduleService.convertAllEntityToDtoWithSelected(scheduleRepository.findAll(), selectedStop.getSchedule()));
        }
    }

    public void saveStop(String timeBegining, String timeEnd, String schedule, String station, Integer stopId){

        String[] fullTimeBegining = timeBegining.split(":");
        Integer hourOfBegining = Integer.valueOf(fullTimeBegining[0]);
        Integer minuteOfBegining = Integer.valueOf(fullTimeBegining[1]);
        LocalTime localTimeBegining = LocalTime.of(hourOfBegining, minuteOfBegining, 0);

        String[] fullTimeEnd = timeEnd.split(":");
        Integer hourOfEnd = Integer.valueOf(fullTimeEnd[0]);
        Integer minuteOfEnd = Integer.valueOf(fullTimeEnd[1]);
        LocalTime localTimeEnd = LocalTime.of(hourOfEnd, minuteOfEnd, 0);

        String[] fullName = schedule.split("_->_");
        String numberOfTrain = fullName[0];
        String timeOfSchedule = fullName[1];
        if (stopId.equals(0)) {
            Stop stopChanged = new Stop(
                    localTimeBegining,
                    localTimeEnd,
                    //scheduleRepository.findByTime(schedule),
                    scheduleRepository.findByTimeAndTrainNumber(timeOfSchedule, numberOfTrain),
                    stationRepository.findByName(station)
            );
            stopRepository.save(stopChanged);
        } else {
            Stop stopChanged = stopRepository.findById(stopId);
            boolean wasChanged = false;
            if(!stopChanged.getTimeBegining().equals(localTimeBegining)){
                stopChanged.setTimeBegining(localTimeBegining);
                wasChanged = true;
            }
            if(!stopChanged.getTimeEnd().equals(localTimeEnd)){
                stopChanged.setTimeEnd(localTimeEnd);
                wasChanged = true;
            }
            //Schedule scheduleNew = scheduleRepository.findByTime(schedule);
            Schedule scheduleNew = scheduleRepository.findByTimeAndTrainNumber(timeOfSchedule, numberOfTrain);
            if(!stopChanged.getSchedule().equals(scheduleNew)){
                stopChanged.setSchedule(scheduleNew);
                wasChanged = true;
            }
            Station stationNew = stationRepository.findByName(station);
            if(!stopChanged.getStation().equals(stationNew)){
                stopChanged.setStation(stationNew);
                wasChanged = true;
            }
            if(wasChanged){
                stopRepository.save(stopChanged);
            }
        }
    }
}
