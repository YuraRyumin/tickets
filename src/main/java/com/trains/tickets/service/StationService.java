package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Station;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.dto.StationsForMainDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StationService {
    public Iterable<StationsForMainDTO> convertAllEntityToDto(Iterable<Station> stations){
        return StreamSupport.stream(stations.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Iterable<StationsForMainDTO> convertAllEntityToDtoWithSelected(Iterable<Station> stations, Station selectedStation){
        return StreamSupport.stream(stations.spliterator(), false)
                .map(station -> {
                    StationsForMainDTO stationDTO = convertEntityToDto(station);
                    if (station.getName().equals(selectedStation.getName())){
                        stationDTO.setSelected(true);
                    } else {
                        stationDTO.setSelected(false);
                    }
                    return stationDTO;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public StationsForMainDTO convertEntityToDto(Station station) {
        StationsForMainDTO stationsForMainDTO = new StationsForMainDTO();

        stationsForMainDTO.setId(station.getId());
        stationsForMainDTO.setName(station.getName());

        return stationsForMainDTO;
    }

    public Iterable<StationsForMainDTO> convertAllEntitysToDto(Iterable<Station> stations) {
        return StreamSupport.stream(stations.spliterator(), false)
                .map(station -> {StationsForMainDTO stationForMainDTO = new StationsForMainDTO();
                    stationForMainDTO.setId(station.getId());
                    stationForMainDTO.setName(station.getName());
                    return  stationForMainDTO;})
                //.sorted((h1, h2) -> h1.getName().compareTo(h2.getName()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public StationsForMainDTO getEmptyDto(){
        StationsForMainDTO stationsForMainDTO = new StationsForMainDTO();

        stationsForMainDTO.setId(0);
        stationsForMainDTO.setName("");

        return stationsForMainDTO;
    }
}
