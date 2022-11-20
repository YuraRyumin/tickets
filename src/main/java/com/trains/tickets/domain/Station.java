package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "stations")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Integer id_schedule;

    public Station() {
    }

    public Station(String name, Integer id_schedule) {
        this.name = name;
        this.id_schedule = id_schedule;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId_schedule() {
        return id_schedule;
    }

    public void setId_schedule(Integer id_schedule) {
        this.id_schedule = id_schedule;
    }
}
