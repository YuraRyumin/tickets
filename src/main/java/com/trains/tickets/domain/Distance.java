package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "distances")
public class Distance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_station_first")
    private Station stationFirst;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_station_last")
    private Station stationLast;
    @Column(name = "km")
    private Integer kilometers;

    public Distance() {
    }

    public Distance(Station stationFirst, Station stationLast, Integer kilometers) {
        this.stationFirst = stationFirst;
        this.stationLast = stationLast;
        this.kilometers = kilometers;
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
        this.stationFirst = stationFirst;
    }

    public Station getStationLast() {
        return stationLast;
    }

    public void setStationLast(Station stationLast) {
        this.stationLast = stationLast;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer km) {
        this.kilometers = km;
    }
}
