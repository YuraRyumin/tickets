package com.trains.tickets.repository;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Ticket;
import com.trains.tickets.domain.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findById(Integer id);
    Set<Ticket> findAllByUserLogin(String login);
    Set<Ticket> findAllByDateTicketAndScheduleTimeAndWagonId(LocalDate dateTicket, String schedule, Integer wagonId);
    Ticket findByPassengerAndDateTicketAndTrainAndSchedule(Passenger passenger, LocalDate localDate, Train train, Schedule schedule);
    Ticket findBySeatAndDateTicketAndTrainAndSchedule(Integer seat, LocalDate localDate, Train train, Schedule schedule);
    Set<Ticket> findAllByDateTicketAndTrainAndSchedule(LocalDate localDate, Train train, Schedule schedule);

}
