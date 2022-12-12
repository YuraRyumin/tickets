package com.trains.tickets.service;

import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.Train;
import com.trains.tickets.dto.StationsForMainDTO;
import com.trains.tickets.dto.TrainDTO;
import com.trains.tickets.repository.TrainRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TrainService {
    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

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

    public void putInfoAboutTrainToModel(String train, Model model){
        if (train.equals("new")) {
            model.addAttribute("train", getEmptyDto());
        } else {
            model.addAttribute("train", convertEntityToDto(trainRepository.findById(Integer.parseInt(train))));
        }
    }

    public void saveTrain(String number, Integer seats, Integer trainId){
        if (trainId.equals(0)) {
            Train trainChanged = new Train(
                    number,
                    seats
            );
            trainRepository.save(trainChanged);
        } else {
            Train trainChanged = trainRepository.findById(trainId);
            if (trainChanged == null){
                throw  new NullPointerException("Train not found!");
            }
            boolean wasChanged = false;
            if(!trainChanged.getNumber().equals(number)){
                trainChanged.setNumber(number);
                wasChanged = true;
            }
            if(!trainChanged.getSeats().equals(seats)){
                trainChanged.setSeats(seats);
                wasChanged = true;
            }
            if(wasChanged){
                trainRepository.save(trainChanged);
            }
        }
    }
}
