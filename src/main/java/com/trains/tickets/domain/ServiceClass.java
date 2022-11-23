package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "service_classes")
public class ServiceClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "prise_per_km")
    private Float prisePerKm;

    public ServiceClass() {
    }

    public ServiceClass(String name, Float prisePerKm) {
        this.name = name;
        this.prisePerKm = prisePerKm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrisePerKm() {
        return prisePerKm;
    }

    public void setPrisePerKm(Float prisePerKm) {
        this.prisePerKm = prisePerKm;
    }
}
