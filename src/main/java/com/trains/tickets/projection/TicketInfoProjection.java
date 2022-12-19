package com.trains.tickets.projection;

public interface TicketInfoProjection {
    String getStationFirst();
    String getStationLast();
    String getTimeDeparture();
    String getTimeArrival();
    String getPassengerName();
    String getPassengerSurname();
    String getPassengerGender();
    String getPassengerPassport();
    String getPassengerDate();
    String getTrainNumber();
    String getTrainId();
    String getSchedule();
    Integer getScheduleID();
    String getSchedName();
}
