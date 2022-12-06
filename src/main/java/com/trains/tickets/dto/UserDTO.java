package com.trains.tickets.dto;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Role;
import lombok.Data;

import javax.persistence.*;

@Data
public class UserDTO {
    private Integer id;
    private String email;
    private String telephone;
    private String login;
    private String password;
    private String activationCode;
    private String passenger;
    private String role;
    private boolean active;
}
