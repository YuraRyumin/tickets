package com.trains.tickets.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TicketFinder {
    private String stationFirst;
    private String stationLast;

    public TicketFinder() {
    }

    public String getStationFirst() {
        return stationFirst;
    }

    public String getStationLast() {
        return stationLast;
    }

    public void setStationFirst(String stationFirst) {
        this.stationFirst = stationFirst;
    }

    public void setStationLast(String stationLast) {
        this.stationLast = stationLast;
    }


}
