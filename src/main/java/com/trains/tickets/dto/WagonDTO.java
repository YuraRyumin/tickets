package com.trains.tickets.dto;

import lombok.Data;

@Data
public class WagonDTO {
    private Integer id;
    private String train;
    private String serviceClasses;
    private String name;
    private Integer seats;
    private boolean selected;
}
