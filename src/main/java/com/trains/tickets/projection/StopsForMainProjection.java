package com.trains.tickets.projection;

public interface StopsForMainProjection {
    Integer getId();
    String getStationFirst();
    String getStationLast();
    String getTimeDeparture();
    String getTimeArrival();
}
