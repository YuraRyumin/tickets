package com.trains.tickets.service;

import com.trains.tickets.domain.Station;
import com.trains.tickets.dto.StationsForMainDTO;
import com.trains.tickets.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

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
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public StationsForMainDTO getEmptyDto(){
        StationsForMainDTO stationsForMainDTO = new StationsForMainDTO();

        stationsForMainDTO.setId(0);
        stationsForMainDTO.setName("");

        return stationsForMainDTO;
    }

    public void putInfoAboutStationToModel(String station, Model model){
        if (station.equals("new")) {
            model.addAttribute("stations", getEmptyDto());
        } else {
            Station selecteStation = stationRepository.findById(Integer.parseInt(station));
            if(selecteStation == null){
                throw  new NullPointerException("Station not found!");
            }
            model.addAttribute("stations", convertEntityToDto(selecteStation));
        }
    }

    public void saveStation(String name, Integer stationId){
        if(stationId.equals(0)){
            Station stationChanged = new Station(name);
            stationRepository.save(stationChanged);
        } else {
            Station stationChanged = stationRepository.findById(stationId);
            if(!stationChanged.getName().equals(name)){
                stationChanged.setName(name);
                stationRepository.save(stationChanged);
            }
        }
    }
}
