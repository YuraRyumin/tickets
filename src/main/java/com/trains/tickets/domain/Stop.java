package com.trains.tickets.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "stops")
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "time_begining")
    private LocalTime timeBegining;
    @Column(name = "time_end")
    private LocalTime timeEnd;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_schedule")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_station")
    private Station station;

    public Stop() {
    }

    public Stop(LocalTime timeBegining, LocalTime timeEnd, Schedule schedule, Station station) {
        this.timeBegining = timeBegining;
        this.timeEnd = timeEnd;
        this.schedule = schedule;
        this.station = station;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getTimeBegining() {
        return timeBegining;
    }

    public void setTimeBegining(LocalTime timeBegining) {
        this.timeBegining = timeBegining;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
