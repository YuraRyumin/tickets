package com.trains.tickets.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalTime;

@Data
public class StopDTO {
    private Integer id;
    private String station;
    private String schedule;
    private LocalTime timeBegining;
    private LocalTime timeEnd;
}
