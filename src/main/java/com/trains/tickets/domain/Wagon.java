package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "wagons")
public class Wagon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_train")
    private Train train;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_service_classes")
    private ServiceClass serviceClasses;
    private String name;
    private Integer seats;

    public Wagon() {
    }

    public Wagon(Train train, ServiceClass serviceClasses, String name, Integer seats) {
        this.train = train;
        this.serviceClasses = serviceClasses;
        this.name = name;
        this.seats = seats;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public ServiceClass getServiceClasses() {
        return serviceClasses;
    }

    public void setServiceClasses(ServiceClass serviceClasses) {
        this.serviceClasses = serviceClasses;
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
