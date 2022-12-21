package com.trains.tickets.service;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.DistanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DistancesServiceTest {
    @Mock
    DistanceRepository distanceRepositoryMockito;

    @InjectMocks
    DistancesService distancesService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveDistance(){
        when(distanceRepositoryMockito.save(any(Distance.class))).thenReturn(new Distance());
        Assertions.assertDoesNotThrow(() -> distancesService.saveDistance(0, 10,"Abc", "Cba", new User()));
    }

    @Test
    void anotherSaveDistance(){
        Distance distance = new Distance(new Station("AAA"), new Station("BBB"), 15);
        distance.setId(10);
        when(distanceRepositoryMockito.findById(10)).thenReturn(distance);
        when(distanceRepositoryMockito.save(distance)).thenReturn(distance);
        Assertions.assertDoesNotThrow(() -> distancesService.saveDistance(distance.getId(), 11, "FFF", "TTT", new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(distancesService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(distancesService.getEmptyDto().getStationFirst(), "");
        Assertions.assertEquals(distancesService.getEmptyDto().getStationLast(), "");
        Assertions.assertEquals(distancesService.getEmptyDto().getKilometers(), 0);
    }
}