package com.trains.tickets.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
    private Integer id;
    private Character time;
    private Integer dayOfWeek;
    private String train;
}
