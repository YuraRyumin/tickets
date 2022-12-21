package com.trains.tickets.service;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.Train;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.TrainRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TrainServiceTest {
    @Mock
    TrainRepository trainRepositoryMockito;

    @InjectMocks
    TrainService trainService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTrain() {
        when(trainRepositoryMockito.save(any(Train.class))).thenReturn(new Train());
        Assertions.assertDoesNotThrow(() -> trainService.saveTrain("Abc", 0, new User()));
    }

    @Test
    void anotherSaveTrain(){
        Train train = new Train("Abs");
        train.setId(4);
        when(trainRepositoryMockito.findById(4)).thenReturn(train);
        when(trainRepositoryMockito.save(train)).thenReturn(train);
        Assertions.assertDoesNotThrow(() -> trainService.saveTrain(train.getNumber() + "1", train.getId(), new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(trainService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(trainService.getEmptyDto().getNumber(), "");
    }
}