package com.trains.tickets.dto;

import com.trains.tickets.domain.Station;
import lombok.Data;

@Data
public class DistanceDTO {
    private Integer id;
    private String stationFirst;
    private String stationLast;
    private Integer kilometers;
}
