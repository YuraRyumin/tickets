package com.trains.tickets.dto;

import lombok.Data;

@Data
public class DistanceDTO {
    private Integer id;
    private String stationFirst;
    private String stationLast;
    private Integer kilometers;
}
