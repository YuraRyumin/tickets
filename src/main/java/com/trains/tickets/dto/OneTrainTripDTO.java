package com.trains.tickets.dto;

import lombok.Data;

@Data
public class OneTrainTripDTO {
    private Integer id;
    private String stationFirst;
    private String stationLast;
    private String timeDeparture;
    private String timeArrival;
    private String schedule;
    private String train;
}
