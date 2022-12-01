package com.trains.tickets.projection;

public interface StopsDirectProjection {
    Integer getId();
    String getStationFirst();
    String getStationLast();
    String getTimeDeparture();
    String getTimeArrival();
}
