package com.trains.tickets.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer id_passenger;
    private LocalDate date_ticket;
    private Integer id_train;
    private Integer id_wagons;

    public Ticket() {
    }

    public Ticket(Integer id_passenger, LocalDate date_ticket, Integer id_train, Integer id_wagons) {
        this.id_passenger = id_passenger;
        this.date_ticket = date_ticket;
        this.id_train = id_train;
        this.id_wagons = id_wagons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_passenger() {
        return id_passenger;
    }

    public void setId_passenger(Integer id_passenger) {
        this.id_passenger = id_passenger;
    }

    public LocalDate getDate_ticket() {
        return date_ticket;
    }

    public void setDate_ticket(LocalDate date_ticket) {
        this.date_ticket = date_ticket;
    }

    public Integer getId_train() {
        return id_train;
    }

    public void setId_train(Integer id_train) {
        this.id_train = id_train;
    }

    public Integer getId_wagons() {
        return id_wagons;
    }

    public void setId_wagons(Integer id_wagons) {
        this.id_wagons = id_wagons;
    }
}
