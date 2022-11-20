package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "service_classes")
public class ServiceClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Float prise_per_km;

    public ServiceClass() {
    }

    public ServiceClass(String name, Float prise_per_km) {
        this.name = name;
        this.prise_per_km = prise_per_km;
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

    public Float getPrise_per_km() {
        return prise_per_km;
    }

    public void setPrise_per_km(Float prise_per_km) {
        this.prise_per_km = prise_per_km;
    }
}
