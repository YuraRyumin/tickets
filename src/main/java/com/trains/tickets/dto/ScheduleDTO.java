package com.trains.tickets.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
    private Integer id;
    private String time;
    private String dayOfWeek;
    private String train;
    private boolean selected;
    private String name;
}
