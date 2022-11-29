package com.trains.tickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
public class StopsForMainDTO {
    @Value("#{target.id}")
    private Integer id;
    @Value("#{target.stationFirst}")
    private String stationFirst;
    @Value("#{target.stationLast}")
    private String stationLast;
    @Value("#{target.timeDeparture}")
    private String timeDeparture;
    @Value("#{target.timeArrival}")
    private String timeArrival;

}
