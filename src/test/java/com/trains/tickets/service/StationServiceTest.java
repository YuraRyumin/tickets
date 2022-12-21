package com.trains.tickets.service;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.StationRepository;
import jdk.jfr.StackTrace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class StationServiceTest {
    @Mock
    StationRepository stationRepositoryMockito;

    @InjectMocks
    StationService stationService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveStation() {
        when(stationRepositoryMockito.save(any(Station.class))).thenReturn(new Station());
        Assertions.assertDoesNotThrow(() -> stationService.saveStation("Abc", 0, new User()));
    }

    @Test
    void anotherSaveStation(){
        Station station = new Station("Abs");
        station.setId(4);
        when(stationRepositoryMockito.findById(4)).thenReturn(station);
        when(stationRepositoryMockito.save(station)).thenReturn(station);
        Assertions.assertDoesNotThrow(() -> stationService.saveStation(station.getName() + "1", station.getId(), new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(stationService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(stationService.getEmptyDto().getName(), "");
    }
}