package com.trains.tickets.service;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Train;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.ScheduleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ScheduleServiceTest {
    @Mock
    ScheduleRepository scheduleRepositoryMockito;

    @InjectMocks
    ScheduleService scheduleService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveSchedule() {
        when(scheduleRepositoryMockito.save(any(Schedule.class))).thenReturn(new Schedule());
        Assertions.assertDoesNotThrow(() -> scheduleService.saveSchedule("Abc", 5, "TTT", 0, new User()));
    }

    @Test
    void anotherSaveSchedule(){
        Schedule schedule = new Schedule("11:11", 5, new Train());
        schedule.setId(5);
        when(scheduleRepositoryMockito.findById(5)).thenReturn(schedule);
        when(scheduleRepositoryMockito.save(schedule)).thenReturn(schedule);
        Assertions.assertDoesNotThrow(() -> scheduleService.saveSchedule("12:12", 6, "TT", schedule.getId(), new User()));
    }

    @Test
    void testNameOfDay(){
        Assertions.assertEquals(scheduleService.returnDaysOfWeek(1), "Sunday");
    }

    @Test
    void testDaysOfWeek(){
        Assertions.assertEquals(scheduleService.returnDaysOfWeek(1).size(), 7);
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(scheduleService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(scheduleService.getEmptyDto().getTrain(), "");
        Assertions.assertEquals(scheduleService.getEmptyDto().getTime(), "");
        Assertions.assertEquals(scheduleService.getEmptyDto().getDayOfWeek(), "");
    }
}