package com.trains.tickets.service;

import com.trains.tickets.domain.Station;
import com.trains.tickets.dto.StationsForMainDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StationService {
    private StationsForMainDTO convertEntityToDto(Station station) {
        StationsForMainDTO stationsForMainDTO = new StationsForMainDTO();

        stationsForMainDTO.setId(station.getId());
        stationsForMainDTO.setName(station.getName());

        return stationsForMainDTO;
    }

    public Iterable<StationsForMainDTO> convertEntityToDto(Iterable<Station> stations) {
        return StreamSupport.stream(stations.spliterator(), false)
                .map(station -> {StationsForMainDTO stationForMainDTO = new StationsForMainDTO();
                    stationForMainDTO.setId(station.getId());
                    stationForMainDTO.setName(station.getName());
                    return  stationForMainDTO;})
                //.sorted(Comparator.comparing(StationsForMainDTO::getName))
                .sorted((h1, h2) -> h1.getName().compareTo(h2.getName()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
