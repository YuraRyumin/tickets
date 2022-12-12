package com.trains.tickets.service;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.DistanceDTO;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.repository.PassengerRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public Iterable<PassengerDTO> convertAllEntityToDto(Iterable<Passenger> passengers){
        return StreamSupport.stream(passengers.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Iterable<PassengerDTO> convertAllEntityToDtoWithSelected(Iterable<Passenger> passengers, Passenger selectedPassenger){
        return StreamSupport.stream(passengers.spliterator(), false)
                .map(passenger -> {
                    PassengerDTO passengerDTO = convertEntityToDto(passenger);
                    if(selectedPassenger == null){
                        passengerDTO.setSelected(false);
                    } else {
                        if (passenger.getSurname().equals(selectedPassenger.getSurname()) &&
                                passenger.getName().equals(selectedPassenger.getName())) {
                            passengerDTO.setSelected(true);
                        } else {
                            passengerDTO.setSelected(false);
                        }
                    }
                    return passengerDTO;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public PassengerDTO convertEntityToDto(Passenger passenger){
        PassengerDTO passengerDTO = new PassengerDTO();

        passengerDTO.setId(passenger.getId());
        passengerDTO.setName(passenger.getName());
        passengerDTO.setSurname(passenger.getSurname());
        passengerDTO.setPassport(passenger.getPassport());
        passengerDTO.setGender(passenger.getGender());
        passengerDTO.setDateOfBirth(passenger.getDateOfBirth());
        passengerDTO.setFullName(passenger.getName() + " " + passenger.getSurname());

        return passengerDTO;
    }

    public PassengerDTO getEmptyDto(){
        PassengerDTO passengerDTO = new PassengerDTO();

        passengerDTO.setId(0);
        passengerDTO.setName("");
        passengerDTO.setSurname("");
        passengerDTO.setPassport("");
        passengerDTO.setGender("");
        passengerDTO.setDateOfBirth(LocalDate.of(1, 1, 1));

        return passengerDTO;
    }

    public void savePassenger(String name,
                           String surname,
                           String passport,
                           String gender,
                           String dateOfBirth,
                           Integer passengerId){
        String[] fullDate = dateOfBirth.split("-");
        Integer dayOfBirth = Integer.valueOf(fullDate[2]);
        Integer monthOfBirth = Integer.valueOf(fullDate[1]);
        Integer yearOfBirth = Integer.valueOf(fullDate[0]);
        LocalDate localDateOfBirth = LocalDate.of(yearOfBirth,monthOfBirth,dayOfBirth);
        System.out.println(dateOfBirth);
        if (passengerId.equals(0)) {
            Passenger passengerChanged = new Passenger(
                    name,
                    surname,
                    passport,
                    gender,
                    localDateOfBirth
            );
            passengerRepository.save(passengerChanged);
        } else {
            Passenger passengerChanged = passengerRepository.findById(passengerId);
            boolean wasChanged = false;
            if(!passengerChanged.getName().equals(name)){
                passengerChanged.setName(name);
                wasChanged = true;
            }
            if(!passengerChanged.getSurname().equals(surname)){
                passengerChanged.setSurname(surname);
                wasChanged = true;
            }
            if(!passengerChanged.getPassport().equals(passport)){
                passengerChanged.setPassport(passport);
                wasChanged = true;
            }
            if(!passengerChanged.getGender().equals(gender)){
                passengerChanged.setGender(gender);
                wasChanged = true;
            }
            if(!passengerChanged.getDateOfBirth().equals(localDateOfBirth)){
                passengerChanged.setDateOfBirth(localDateOfBirth);
                wasChanged = true;
            }
            if(wasChanged){
                passengerRepository.save(passengerChanged);
            }
        }
    }

    public void putInfoAboutPassengerToModel(Model model, String passenger){
        if (passenger.equals("new")) {
            model.addAttribute("passenger", getEmptyDto());
        } else {
            model.addAttribute("passenger", convertEntityToDto(passengerRepository.findById(Integer.parseInt(passenger))));
        }
    }
}
