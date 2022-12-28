package com.trains.tickets.service;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.ServiceClassRepository;
import com.trains.tickets.repository.TrainRepository;
import com.trains.tickets.repository.WagonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class WagonServiceTest {
    @Mock
    WagonRepository wagonRepositoryMockito;

    @Mock
    ServiceClassRepository serviceClassRepository;

    @Mock
    TrainRepository trainRepository;

    @InjectMocks
    WagonService wagonService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveWagon() {
//        when(wagonRepositoryMockito.save(any(Wagon.class))).thenReturn(new Wagon());
//        Assertions.assertDoesNotThrow(() -> wagonService.saveWagon("train",
//                                                                    "serviceClasses",
//                                                                    "name",
//                                                                    1,
//                                                                    0,
//                                                                    new User()));
    }

    @Test
    void anotherSaveWagon(){
//        Wagon wagon = new Wagon(new Train(), new ServiceClass(), "name", 5);
//        wagon.setId(4);
//        when(wagonRepositoryMockito.findById(4)).thenReturn(wagon);
//        when(wagonRepositoryMockito.save(wagon)).thenReturn(wagon);
//        Assertions.assertDoesNotThrow(() -> wagonService.saveWagon("train",
//                                                                    "serviceClasses",
//                                                                    "name",
//                                                                    1,
//                                                                    4,
//                                                                    new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(wagonService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(wagonService.getEmptyDto().getName(), "");
        Assertions.assertEquals(wagonService.getEmptyDto().getServiceClasses(), "");
        Assertions.assertEquals(wagonService.getEmptyDto().getTrain(), "");
        Assertions.assertEquals(wagonService.getEmptyDto().getSeats(), 0);
    }
}