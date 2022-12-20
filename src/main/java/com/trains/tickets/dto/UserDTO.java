package com.trains.tickets.dto;

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
    private String uuid;
    private String role;
    private boolean active;
    private boolean selected;
}
