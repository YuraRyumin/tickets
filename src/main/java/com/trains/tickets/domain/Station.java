package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "stations")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_schedule")
    private Schedule schedule;

    public Station() {
    }

    public Station(String name, Schedule schedule) {
        this.name = name;
        this.schedule = schedule;
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

    public Schedule getId_schedule() {
        return schedule;
    }

    public void setId_schedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
