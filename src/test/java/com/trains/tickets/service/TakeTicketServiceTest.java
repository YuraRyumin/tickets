package com.trains.tickets.service;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.TicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TakeTicketServiceTest {
    @Mock
    TicketRepository ticketRepositoryMockito;

    @InjectMocks
    TakeTicketService takeTicketService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTakeTicket() {
//        when(ticketRepositoryMockito.save(any(Role.class))).thenReturn(new Role());
//        Assertions.assertDoesNotThrow(() -> takeTicketService.createTicket("Abc", 0, new User()));
    }

    @Test
    void anotherSaveTakeTicket(){
//        Role role = new Role("Abs");
//        role.setId(4);
//        when(ticketRepositoryMockito.findById(4)).thenReturn(role);
//        when(ticketRepositoryMockito.save(role)).thenReturn(role);
//        Assertions.assertDoesNotThrow(() -> takeTicketService.createTicket(role.getName() + "1", role.getId(), new User()));
    }
}