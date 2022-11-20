package com.trains.tickets.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String email;
    private String telephone;
    private String login;
    private String password;
    private Integer id_passenger;
    private Integer id_role;
    private boolean active;

    public User() {
    }

    public User(String email, String telephone, String login, String password, Integer id_passenger, Integer id_role, boolean active) {
        this.email = email;
        this.telephone = telephone;
        this.login = login;
        this.password = password;
        this.id_passenger = id_passenger;
        this.id_role = id_role;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId_passenger() {
        return id_passenger;
    }

    public void setId_passenger(Integer id_passenger) {
        this.id_passenger = id_passenger;
    }

    public Integer getId_role() {
        return id_role;
    }

    public void setId_role(Integer id_role) {
        this.id_role = id_role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
