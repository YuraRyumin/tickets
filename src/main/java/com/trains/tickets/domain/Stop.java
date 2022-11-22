package com.trains.tickets.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stops")
public class Stop {
    @Id

    private LocalDate time_begining;
    private LocalDate time_end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_schedule")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_station")
    private Station station;

    public Stop() {
    }

    public Stop(LocalDate time_begining, LocalDate time_end, Schedule schedule, Station station) {
        this.time_begining = time_begining;
        this.time_end = time_end;
        this.schedule = schedule;
        this.station = station;
    }

    public LocalDate getTime_begining() {
        return time_begining;
    }

    public void setTime_begining(LocalDate time_begining) {
        this.time_begining = time_begining;
    }

    public LocalDate getTime_end() {
        return time_end;
    }

    public void setTime_end(LocalDate time_end) {
        this.time_end = time_end;
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
