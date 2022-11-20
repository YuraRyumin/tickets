package com.trains.tickets.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "distances")
public class Distance {
    @Id

    private Integer id_station_first;
    private Integer id_station_last;
    private Integer km;

    public Distance() {
    }

    public Distance(Integer id_station_first, Integer id_station_last, Integer km) {
        this.id_station_first = id_station_first;
        this.id_station_last = id_station_last;
        this.km = km;
    }

    public Integer getId_station_first() {
        return id_station_first;
    }

    public void setId_station_first(Integer id_station_first) {
        this.id_station_first = id_station_first;
    }

    public Integer getId_station_last() {
        return id_station_last;
    }

    public void setId_station_last(Integer id_station_last) {
        this.id_station_last = id_station_last;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

}
