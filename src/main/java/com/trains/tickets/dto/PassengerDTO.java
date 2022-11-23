package com.trains.tickets.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PassengerDTO {
    private Integer id;
    private String name;
    private String surname;
    private String passport;
    private String gender;
    private LocalDate dateOfBirth;
}
