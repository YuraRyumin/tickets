package com.trains.tickets.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "stops")
public class Stop {
    @Id

    private LocalDate time_begining;
    private LocalDate time_end;
    private Integer id_schedule;
    private Integer id_station;

    public Stop() {
    }

    public Stop(LocalDate time_begining, LocalDate time_end, Integer id_schedule, Integer id_station) {
        this.time_begining = time_begining;
        this.time_end = time_end;
        this.id_schedule = id_schedule;
        this.id_station = id_station;
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

    public Integer getId_schedule() {
        return id_schedule;
    }

    public void setId_schedule(Integer id_schedule) {
        this.id_schedule = id_schedule;
    }

    public Integer getId_station() {
        return id_station;
    }

    public void setId_station(Integer id_station) {
        this.id_station = id_station;
    }
}
