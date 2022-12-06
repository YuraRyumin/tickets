package com.trains.tickets.service;

import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.Train;
import com.trains.tickets.dto.StationsForMainDTO;
import com.trains.tickets.dto.TrainDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TrainService {
    public Iterable<TrainDTO> convertAllEntityToDto(Iterable<Train> trains){
        return StreamSupport.stream(trains.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Iterable<TrainDTO> convertAllEntityToDtoWithSelected(Iterable<Train> trains, Train selectedTrain){
        return StreamSupport.stream(trains.spliterator(), false)
                .map(train -> {
                    TrainDTO trainDTO = convertEntityToDto(train);
                    if (train.getNumber().equals(selectedTrain.getNumber())){
                        trainDTO.setSelected(true);
                    } else {
                        trainDTO.setSelected(false);
                    }
                    return trainDTO;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public TrainDTO convertEntityToDto(Train train){
        TrainDTO trainDTO = new TrainDTO();

        trainDTO.setId(train.getId());
        trainDTO.setNumber(train.getNumber());
        trainDTO.setSeats(train.getSeats());

        return trainDTO;
    }

    public TrainDTO getEmptyDto(){
        TrainDTO trainDTO = new TrainDTO();

        trainDTO.setId(0);
        trainDTO.setNumber("");
        trainDTO.setSeats(0);

        return trainDTO;
    }
}
