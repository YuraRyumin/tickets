package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Ticket;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.dto.TicketDTO;
import com.trains.tickets.dto.TicketForUserDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TicketService {
    public TicketForUserDTO convertEntityForMainToDto(Ticket ticket){
        TicketForUserDTO ticketForUserDTO = new TicketForUserDTO();

        Passenger passenger = ticket.getPassenger();
        ticketForUserDTO.setName(passenger.getName());
        ticketForUserDTO.setSurname(passenger.getSurname());
        ticketForUserDTO.setPassport(passenger.getPassport());
        ticketForUserDTO.setDateOfBirth(passenger.getDateOfBirth());
        ticketForUserDTO.setGender(passenger.getGender());

        ticketForUserDTO.setDateTicket(ticket.getDateTicket());

        ticketForUserDTO.setTrainNumber(ticket.getTrain().getNumber());
        ticketForUserDTO.setWagonNumber(ticket.getWagon().getName());
        ticketForUserDTO.setSeat(1);
        ticketForUserDTO.setServiceClass(ticket.getWagon().getServiceClasses().getName());
        ticketForUserDTO.setPrice(ticket.getPrice());

        return ticketForUserDTO;
    }

    public Iterable<TicketDTO> convertAllEntityToDto(Iterable<Ticket> tickets){
        return StreamSupport.stream(tickets.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public TicketDTO convertEntityToDto(Ticket ticket){
        TicketDTO ticketDTO = new TicketDTO();

        ticketDTO.setId(ticket.getId());
        ticketDTO.setPassenger(ticket.getPassenger().getName() + " " + ticket.getPassenger().getSurname());
        ticketDTO.setDateTicket(ticket.getDateTicket());
        ticketDTO.setTrain(ticket.getTrain().getNumber());
        ticketDTO.setWagon(ticket.getWagon().getName());
        ticketDTO.setPrice(ticket.getPrice());
        ticketDTO.setSchedule(ticket.getSchedule().getTime());
        ticketDTO.setSeat(ticket.getSeat());
        ticketDTO.setUser(ticket.getUser().getLogin());

        return ticketDTO;
    }

    public TicketDTO getEmptyDto(){
        TicketDTO ticketDTO = new TicketDTO();

        ticketDTO.setId(0);
        ticketDTO.setPassenger("");
        ticketDTO.setDateTicket(LocalDate.of(1, 1, 1));
        ticketDTO.setTrain("");
        ticketDTO.setWagon("");
        ticketDTO.setPrice(0);
        ticketDTO.setSchedule("");
        ticketDTO.setSeat(0);
        ticketDTO.setUser("");

        return ticketDTO;
    }
}
