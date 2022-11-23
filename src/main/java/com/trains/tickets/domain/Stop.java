package com.trains.tickets.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stops")
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "time_begining")
    private LocalDate timeBegining;
    @Column(name = "time_end")
    private LocalDate timeEnd;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_schedule")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_station")
    private Station station;

    public Stop() {
    }

    public Stop(LocalDate timeBegining, LocalDate timeEnd, Schedule schedule, Station station) {
        this.timeBegining = timeBegining;
        this.timeEnd = timeEnd;
        this.schedule = schedule;
        this.station = station;
    }

    public LocalDate getTimeBegining() {
        return timeBegining;
    }

    public void setTimeBegining(LocalDate timeBegining) {
        this.timeBegining = timeBegining;
    }

    public LocalDate getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDate timeEnd) {
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
