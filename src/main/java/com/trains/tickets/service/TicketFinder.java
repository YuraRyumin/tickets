package com.trains.tickets.service;

import com.trains.tickets.domain.Station;
import com.trains.tickets.repository.StationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

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
