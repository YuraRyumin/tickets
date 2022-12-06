package com.trains.tickets.service;

import com.trains.tickets.domain.Stop;
import com.trains.tickets.dto.StopDTO;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StopService {
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

    public StopDTO getEmptyDto(){
        StopDTO stopDTO = new StopDTO();

        stopDTO.setId(0);
        stopDTO.setStation("");
        stopDTO.setSchedule("");
        stopDTO.setTimeBegining(LocalTime.of(1, 1, 1));
        stopDTO.setTimeEnd(LocalTime.of(1, 1, 1));

        return stopDTO;
    }
}
