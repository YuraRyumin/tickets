package com.trains.tickets.repository;

import com.trains.tickets.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findById(Integer id);

}
