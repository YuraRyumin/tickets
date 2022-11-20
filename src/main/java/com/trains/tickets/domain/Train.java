package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "trains")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String number;
    private Integer seats;

    public Train() {
    }

    public Train(String number, Integer seats) {
        this.number = number;
        this.seats = seats;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
