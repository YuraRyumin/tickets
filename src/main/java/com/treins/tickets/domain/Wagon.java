package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "wagons")
public class Wagon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer id_train;
    private Integer id_service_classes;
    private String name;
    private Integer seats;

    public Wagon() {
    }

    public Wagon(Integer id_train, Integer id_service_classes, String name, Integer seats) {
        this.id_train = id_train;
        this.id_service_classes = id_service_classes;
        this.name = name;
        this.seats = seats;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_train() {
        return id_train;
    }

    public void setId_train(Integer id_train) {
        this.id_train = id_train;
    }

    public Integer getId_service_classes() {
        return id_service_classes;
    }

    public void setId_service_classes(Integer id_service_classes) {
        this.id_service_classes = id_service_classes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
