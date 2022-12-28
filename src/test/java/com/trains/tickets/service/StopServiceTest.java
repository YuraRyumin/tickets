package com.trains.tickets.service;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.ScheduleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.StopRepository;
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

class StopServiceTest {
    @Mock
    StopRepository stopRepositoryMockito;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    StationRepository stationRepository;

    @InjectMocks
    StopService stopService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveStop() {
        when(stopRepositoryMockito.save(any(Stop.class))).thenReturn(new Stop());
        Assertions.assertDoesNotThrow(() -> stopService.saveStop("10:10", "15:15", "Schedule", "Station", 0, new User()));
    }

    @Test
    void anotherSaveStop(){
        Stop stop = new Stop(LocalTime.of(10, 10),
                LocalTime.of(20, 20),
                new Schedule(),
                new Station());
        stop.setId(4);
        when(stopRepositoryMockito.findById(4)).thenReturn(stop);
        when(stopRepositoryMockito.save(stop)).thenReturn(stop);
        Assertions.assertDoesNotThrow(() -> stopService.saveStop("10:10",
                                                                "15:15",
                                                                "NotNew",
                                                                "New",
                                                                4,
                                                                new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(stopService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(stopService.getEmptyDto().getStation(), "");
        Assertions.assertEquals(stopService.getEmptyDto().getSchedule(), "");
        Assertions.assertEquals(stopService.getEmptyDto().getTimeBegining(), LocalTime.of(1, 1, 1));
        Assertions.assertEquals(stopService.getEmptyDto().getTimeEnd(), LocalTime.of(1, 1, 1));
    }
}