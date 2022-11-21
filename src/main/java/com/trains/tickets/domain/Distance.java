package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "distances")
public class Distance {
    @Id
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_station_first")
    private Station StationFirst;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_station_last")
    private Station StationLast;
    private Integer km;

    public Distance() {
    }

    public Distance(Station StationFirst, Station StationLast, Integer km) {
        this.StationFirst = StationFirst;
        this.StationLast = StationLast;
        this.km = km;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Station getStationFirst() {
        return StationFirst;
    }

    public void setStationFirst(Station stationFirst) {
        StationFirst = stationFirst;
    }

    public Station getStationLast() {
        return StationLast;
    }

    public void setStationLast(Station stationLast) {
        StationLast = stationLast;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }
}
