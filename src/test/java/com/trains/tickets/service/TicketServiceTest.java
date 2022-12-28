package com.trains.tickets.service;

import com.trains.tickets.domain.*;
import com.trains.tickets.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TicketServiceTest {
    @Mock
    TicketRepository ticketRepositoryMockito;

    @Mock
    UserRepository userRepository;

    @Mock
    PassengerRepository passengerRepository;

    @Mock
    WagonRepository wagonRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    TrainRepository trainRepository;

    @InjectMocks
    TicketService ticketService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTicket() {
        when(ticketRepositoryMockito.save(any(Ticket.class))).thenReturn(new Ticket(new Passenger(),
                                                                            LocalDate.now(),
                                                                            new Train(),
                                                                            new Wagon(),
                                                                            100,
                                                                            new Schedule(),
                                                                            10,
                                                                            new User()));
        Assertions.assertDoesNotThrow(() -> ticketService.saveTicket("Alex Apon",
                                                                        LocalDate.now().toString(),
                                                                        "M1",
                                                                        "W1",
                                                                        100,
                                                                        "M1_->_08:37",
                                                                        0,
                                                                        10,
                                                                        "user",
                                                                        new User()));
    }

    @Test
    void anotherSaveTicket(){
        Ticket ticket = new Ticket(new Passenger(),
                                    LocalDate.now(),
                                    new Train(),
                                    new Wagon(),
                                    100,
                                    new Schedule(),
                                    10,
                                    new User());
        ticket.setId(4);
        when(ticketRepositoryMockito.findById(4)).thenReturn(ticket);
        when(ticketRepositoryMockito.save(ticket)).thenReturn(ticket);
        Assertions.assertDoesNotThrow(() -> ticketService.saveTicket("Alex Apon",
                                                                        LocalDate.now().toString(),
                                                                        "M2",
                                                                        "W1",
                                                                        100,
                                                                        "M1_->_08:37",
                                                                        4,
                                                                        10,
                                                                        "user",
                                                                        new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(ticketService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(ticketService.getEmptyDto().getPassenger(), "");
        Assertions.assertEquals(ticketService.getEmptyDto().getDateTicket(), LocalDate.of(1, 1, 1));
        Assertions.assertEquals(ticketService.getEmptyDto().getTrain(), "");
        Assertions.assertEquals(ticketService.getEmptyDto().getWagon(), "");
        Assertions.assertEquals(ticketService.getEmptyDto().getPrice(), 0);
        Assertions.assertEquals(ticketService.getEmptyDto().getSchedule(), "");
        Assertions.assertEquals(ticketService.getEmptyDto().getSeat(), 0);
        Assertions.assertEquals(ticketService.getEmptyDto().getUser(), "");
    }
}