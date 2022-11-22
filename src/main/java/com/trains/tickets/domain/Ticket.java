package com.trains.tickets.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passenger")
    private Passenger passenger;
    private LocalDate date_ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_train")
    private Train train;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_wagons")
    private Wagon wagon;

    public Ticket() {
    }

    public Ticket(Passenger passenger, LocalDate date_ticket, Train train, Wagon wagon) {
        this.passenger = passenger;
        this.date_ticket = date_ticket;
        this.train = train;
        this.wagon = wagon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public LocalDate getDate_ticket() {
        return date_ticket;
    }

    public void setDate_ticket(LocalDate date_ticket) {
        this.date_ticket = date_ticket;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Wagon getWagon() {
        return wagon;
    }

    public void setWagon(Wagon wagon) {
        this.wagon = wagon;
    }
}
