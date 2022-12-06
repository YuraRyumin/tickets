package com.trains.tickets.dto;

import lombok.Data;

@Data
public class ServiceClassDTO {
    private Integer id;
    private String name;
    private Float prisePerKm;
    private boolean selected;
}
