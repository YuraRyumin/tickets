package com.trains.tickets.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "passengers")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@OneToOne(mappedBy = "id_passenger")
    private Integer id;
    private String name;
    private String surname;
    private String passport;
    private String gender;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    public Passenger() {
    }

    public Passenger(String name, String surname, String passport, String gender, LocalDate dateOfBirth) {
        this.name = name;
        this.surname = surname;
        this.passport = passport;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
