package com.trains.tickets.repository;

import com.trains.tickets.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findById(Integer id);
    Set<Ticket> findAllByUserLogin(String login);

}
