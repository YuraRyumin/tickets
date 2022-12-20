package com.trains.tickets.service;

import com.trains.tickets.domain.Train;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.TrainDTO;
import com.trains.tickets.repository.TrainRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Slf4j
@Transactional(readOnly = true)
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

        return trainDTO;
    }

    public TrainDTO getEmptyDto(){
        TrainDTO trainDTO = new TrainDTO();

        trainDTO.setId(0);
        trainDTO.setNumber("");

        return trainDTO;
    }

    public void putInfoAboutTrainToModel(String train, Model model){
        if (train.equals("new")) {
            model.addAttribute("train", getEmptyDto());
        } else {
            model.addAttribute("train", convertEntityToDto(trainRepository.findById(Integer.parseInt(train))));
        }
    }

    @Transactional
    public void saveTrain(String number, Integer trainId, User user){
        if (trainId.equals(0)) {
            Train trainChanged = new Train(
                    number
            );
            trainRepository.save(trainChanged);
            log.error(LocalDateTime.now().toString() + " - " + user.getLogin() + " create new train with id " +
                    trainChanged.getId() + " (" +
                    trainChanged.getNumber() + ")");
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
            if(wasChanged){
                trainRepository.save(trainChanged);
                log.error(LocalDateTime.now().toString() + " - " + user.getLogin() + " change train with id " +
                        trainChanged.getId() + " (" +
                        trainChanged.getNumber() + ")");
            }
        }
    }
}
