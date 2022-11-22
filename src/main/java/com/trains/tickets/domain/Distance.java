package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "distances")
public class Distance {
    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_station_first")
    private Station stationFirst;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_station_last")
    private Station stationLast;
    private Integer km;

    public Distance() {
    }

    public Distance(Station stationFirst, Station stationLast, Integer km) {
        this.stationFirst = stationFirst;
        this.stationLast = stationLast;
        this.km = km;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Station getStationFirst() {
        return stationFirst;
    }

    public void setStationFirst(Station stationFirst) {
        stationFirst = stationFirst;
    }

    public Station getStationLast() {
        return stationLast;
    }

    public void setStationLast(Station stationLast) {
        stationLast = stationLast;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }
}
