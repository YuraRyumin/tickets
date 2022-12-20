package com.trains.tickets.dto;

import lombok.Data;

@Data
public class TicketInfoDTO {
    private String stationFirst;
    private String stationLast;
    private String timeDeparture;
    private String timeArrival;
    private String passengerName;
    private String passengerSurname;
    private String passengerGender;
    private String passengerPassport;
    private String passengerDate;
    private String trainNumber;
    private String trainId;
    private String schedule;
    private Integer scheduleID;
    private String schedName;
}
