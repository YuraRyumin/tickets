package com.trains.tickets.service;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Stop;
import com.trains.tickets.dto.DistanceDTO;
import com.trains.tickets.dto.StopDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DistancesService {
    public Iterable<DistanceDTO> convertAllEntityToDto(Iterable<Distance> distances){
        return StreamSupport.stream(distances.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public DistanceDTO convertEntityToDto(Distance distance){
        DistanceDTO distanceDTO = new DistanceDTO();

        distanceDTO.setId(distance.getId());
        distanceDTO.setStationFirst(distance.getStationFirst().getName());
        distanceDTO.setStationLast(distance.getStationLast().getName());
        distanceDTO.setKilometers(distance.getKilometers());

        return distanceDTO;
    }

    public DistanceDTO getEmptyDto(){
        DistanceDTO distanceDTO = new DistanceDTO();

        distanceDTO.setId(0);
        distanceDTO.setStationFirst("");
        distanceDTO.setStationLast("");
        distanceDTO.setKilometers(0);

        return distanceDTO;
    }
}
