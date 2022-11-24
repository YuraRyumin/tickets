package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Ticket;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.TicketForUserDTO;
import com.trains.tickets.dto.UserDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TicketService {
    private TicketForUserDTO convertEntityToDto(Ticket ticket){
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
}
