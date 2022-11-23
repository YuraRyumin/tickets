package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.repository.PassengerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public List<PassengerDTO> getAllPassengers(){
        return passengerRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private PassengerDTO convertEntityToDto(Passenger passenger){
        PassengerDTO passengerDTO = new PassengerDTO();

        passengerDTO.setId(passenger.getId());
        passengerDTO.setName(passenger.getName());
        passengerDTO.setSurname(passenger.getSurname());
        passengerDTO.setPassport(passenger.getPassport());
        passengerDTO.setGender(passenger.getGender());
        passengerDTO.setDateOfBirth(passenger.getDateOfBirth());

        return passengerDTO;
    }
}
