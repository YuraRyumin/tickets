package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Character time;
    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_train")
    private Train train;

    public Schedule() {
    }

    public Schedule(Character time, Integer dayOfWeek, Train train) {
        this.time = time;
        this.dayOfWeek = dayOfWeek;
        this.train = train;
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

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
}
