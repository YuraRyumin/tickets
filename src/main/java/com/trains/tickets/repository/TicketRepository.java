package com.trains.tickets.repository;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findById(Integer id);
    Set<Ticket> findAllByUserLogin(String login);
    Set<Ticket> findAllByDateTicketAndScheduleTimeAndWagonId(LocalDate dateTicket, String schedule, Integer wagonId);
    Ticket findByDateTicketAndScheduleTimeAndTrainNumberAndPassenger(LocalDate dateTicket, String schedule, String number, Passenger passenger);

}
