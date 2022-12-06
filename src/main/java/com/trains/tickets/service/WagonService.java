package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Wagon;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.dto.WagonDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WagonService {
    public Iterable<WagonDTO> convertAllEntityToDto(Iterable<Wagon> wagons){
        return StreamSupport.stream(wagons.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public WagonDTO convertEntityToDto(Wagon wagon){
        WagonDTO wagonDTO = new WagonDTO();

        wagonDTO.setId(wagon.getId());
        wagonDTO.setTrain(wagon.getTrain().getNumber());
        wagonDTO.setSeats(wagon.getSeats());
        wagonDTO.setServiceClasses(wagon.getServiceClasses().getName());
        wagonDTO.setName(wagon.getName());

        return wagonDTO;
    }

    public WagonDTO getEmptyDto(){
        WagonDTO wagonDTO = new WagonDTO();

        wagonDTO.setId(0);
        wagonDTO.setTrain("");
        wagonDTO.setSeats(0);
        wagonDTO.setServiceClasses("");
        wagonDTO.setName("");

        return wagonDTO;
    }
}
