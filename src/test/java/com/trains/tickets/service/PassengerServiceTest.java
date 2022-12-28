package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.PassengerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PassengerServiceTest {
    @Mock
    PassengerRepository passengerRepositoryMockito;

    @InjectMocks
    PassengerService passengerService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePassenger() {
        when(passengerRepositoryMockito.save(any(Passenger.class))).thenReturn(new Passenger());
        Assertions.assertDoesNotThrow(() -> passengerService.savePassenger("Name",
                                                                        "Surname",
                                                                        "Passport",
                                                                        "FEMALE",
                                                                        LocalDate.now().toString(),
                                                                        0,
                                                                        new User()));
    }

    @Test
    void anotherSavePassenger(){
        Passenger passenger = new Passenger("Name", "Surname", "Passport", "FEMALE", LocalDate.now());
        passenger.setId(3);
        when(passengerRepositoryMockito.findById(3)).thenReturn(passenger);
        when(passengerRepositoryMockito.save(passenger)).thenReturn(passenger);
        Assertions.assertDoesNotThrow(() -> passengerService.savePassenger(passenger.getName() + "1",
                                                                            passenger.getSurname(),
                                                                            passenger.getPassport(),
                                                                            "MALE",
                                                                            LocalDate.now().toString(),
                                                                            passenger.getId(),
                                                                            new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(passengerService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(passengerService.getEmptyDto().getName(), "");
        Assertions.assertEquals(passengerService.getEmptyDto().getSurname(), "");
        Assertions.assertEquals(passengerService.getEmptyDto().getPassport(), "");
        Assertions.assertEquals(passengerService.getEmptyDto().getGender(), "");
        Assertions.assertEquals(passengerService.getEmptyDto().getDateOfBirth(), LocalDate.of(1, 1, 1));
    }
}