package com.trains.tickets.domain;

import javax.persistence.*;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Stop other = (Stop) obj;
        if ((this.station == null) ? (other.station != null) : !this.station.equals(other.station)) {
            return false;
        }
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.timeBegining == null) ? (other.timeBegining != null) : !this.timeBegining.equals(other.timeBegining)) {
            return false;
        }
        if ((this.timeEnd == null) ? (other.timeEnd != null) : !this.timeEnd.equals(other.timeEnd)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.timeEnd != null ? this.timeEnd.hashCode() : 0)
                + (this.timeBegining != null ? this.timeBegining.hashCode() : 0)
                + (this.station != null ? this.station.hashCode() : 0);
        hash = 53 * hash + this.id;
        return hash;
    }
}
