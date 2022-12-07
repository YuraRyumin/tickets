package com.trains.tickets.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_passenger")
    private Passenger passenger;
    @Column(name = "date_ticket")
    private LocalDate dateTicket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_train")
    private Train train;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_wagons")
    private Wagon wagon;
    private Integer price;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_schedule")
    private Schedule schedule;

    public Ticket() {
    }

    public Ticket(Passenger passenger, LocalDate dateTicket, Train train, Wagon wagon, Integer price, Schedule schedule) {
        this.passenger = passenger;
        this.dateTicket = dateTicket;
        this.train = train;
        this.wagon = wagon;
        this.price = price;
        this.schedule = schedule;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public LocalDate getDateTicket() {
        return dateTicket;
    }

    public void setDateTicket(LocalDate dateTicket) {
        this.dateTicket = dateTicket;
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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
