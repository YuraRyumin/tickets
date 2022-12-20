package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.GenderDTO;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.repository.PassengerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Slf4j
@Transactional(readOnly = true)
@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
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
    @Transactional
    public void savePassenger(String name,
                              String surname,
                              String passport,
                              String gender,
                              String dateOfBirth,
                              Integer passengerId,
                              User user){
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
            log.error(LocalDateTime.now().toString() + " - " + user.getLogin() + " create new passenger with id " +
                    passengerChanged.getId() + " (" +
                    passengerChanged.getName() + "; " +
                    passengerChanged.getSurname() + "; " +
                    passengerChanged.getGender() + "; " +
                    passengerChanged.getDateOfBirth().toString() + "; " +
                    passengerChanged.getPassport() + ")");
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
                log.error(LocalDateTime.now().toString() + " - " + user.getLogin() + " change passenger with id " +
                        passengerChanged.getId() + " (" +
                        passengerChanged.getName() + "; " +
                        passengerChanged.getSurname() + "; " +
                        passengerChanged.getGender() + "; " +
                        passengerChanged.getDateOfBirth().toString() + "; " +
                        passengerChanged.getPassport() + ")");
            }
        }
    }

    public Set<GenderDTO> getGendersList(Passenger passenger){
        Set<GenderDTO> genderDTOSet = new HashSet<>();
        String passengerGender = "";
        if(!(passenger == null)){
            passengerGender = passenger.getGender();
        }
        GenderDTO genderDTOFemale = new GenderDTO();
        genderDTOFemale.setName("FEMALE");
        if(passengerGender.equals("FEMALE")){
            genderDTOFemale.setSelected(true);
        } else {
            genderDTOFemale.setSelected(false);
        }
        genderDTOSet.add(genderDTOFemale);
        GenderDTO genderDTOMale = new GenderDTO();
        genderDTOMale.setName("MALE");
        if(passengerGender.equals("MALE")){
            genderDTOMale.setSelected(true);
        } else {
            genderDTOMale.setSelected(false);
        }
        genderDTOSet.add(genderDTOMale);
        return  genderDTOSet;
    }

    public void putInfoAboutPassengerToModel(Model model, String passenger){
        if (passenger.equals("new")) {
            model.addAttribute("passenger", getEmptyDto());
        } else {
            model.addAttribute("passenger", convertEntityToDto(passengerRepository.findById(Integer.parseInt(passenger))));
        }
    }
}
