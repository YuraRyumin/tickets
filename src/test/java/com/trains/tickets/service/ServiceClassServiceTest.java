package com.trains.tickets.service;

import com.trains.tickets.domain.ServiceClass;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.ServiceClassRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ServiceClassServiceTest {
    @Mock
    ServiceClassRepository serviceClassRepositoryMockito;

    @InjectMocks
    ServiceClassService serviceClassService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveServiceClass() {
        when(serviceClassRepositoryMockito.save(any(ServiceClass.class))).thenReturn(new ServiceClass());
        Assertions.assertDoesNotThrow(() -> serviceClassService.saveServiceClass("Abc", Float.valueOf(10), 0, new User()));
    }

    @Test
    void anotherServiceClass(){
        ServiceClass serviceClass = new ServiceClass("Abs", Float.valueOf(10));
        serviceClass.setId(6);
        when(serviceClassRepositoryMockito.findById(6)).thenReturn(serviceClass);
        when(serviceClassRepositoryMockito.save(serviceClass)).thenReturn(serviceClass);
        Assertions.assertDoesNotThrow(() -> serviceClassService.saveServiceClass(serviceClass.getName() + "1",
                                                                                serviceClass.getPrisePerKm(),
                                                                                serviceClass.getId(),
                                                                                new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(serviceClassService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(serviceClassService.getEmptyDto().getName(), "");
        Assertions.assertEquals(serviceClassService.getEmptyDto().getPrisePerKm(), (float) 0);
    }
}