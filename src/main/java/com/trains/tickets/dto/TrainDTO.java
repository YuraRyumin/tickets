package com.trains.tickets.dto;

import lombok.Data;

@Data
public class TrainDTO {
    private Integer id;
    private String number;
    private boolean selected;
}
