package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Character time;
    private Integer day_of_week;
    private Integer id_train;

    public Schedule() {
    }

    public Schedule(Character time, Integer day_of_week, Integer id_train) {
        this.time = time;
        this.day_of_week = day_of_week;
        this.id_train = id_train;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getTime() {
        return time;
    }

    public void setTime(Character time) {
        this.time = time;
    }

    public Integer getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(Integer day_of_week) {
        this.day_of_week = day_of_week;
    }

    public Integer getId_train() {
        return id_train;
    }

    public void setId_train(Integer id_train) {
        this.id_train = id_train;
    }
}
