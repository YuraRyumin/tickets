package com.trains.tickets.service;

import com.trains.tickets.domain.Station;
import com.trains.tickets.dto.StationsForMainDTO;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class StationService {
    private StationsForMainDTO convertEntityToDto(Station station) {
        StationsForMainDTO stationsForMainDTO = new StationsForMainDTO();

        stationsForMainDTO.setId(station.getId());
        stationsForMainDTO.setName(station.getName());

        return stationsForMainDTO;
    }

    public Iterable<StationsForMainDTO> convertEntityToDto(Iterable<Station> stations) {
        Set<StationsForMainDTO> stationsForMainDTO = new HashSet<>();
        for(Station station: stations) {
            StationsForMainDTO stationForMainDTO = new StationsForMainDTO();

            stationForMainDTO.setId(station.getId());
            stationForMainDTO.setName(station.getName());

            stationsForMainDTO.add(stationForMainDTO);
        }
        return stationsForMainDTO;
    }
}
