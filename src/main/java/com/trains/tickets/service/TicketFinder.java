package com.trains.tickets.service;

import com.trains.tickets.domain.Station;
import com.trains.tickets.repository.StationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class TicketFinder {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    private String stationFirst;
    private String stationLast;

    //public TicketFinder(StationRepository stationRepository) {
        //String stationFirst, String stationLast,
        //this.stationFirst = stationFirst;
        //this.stationLast = stationLast;
    //    this.stationRepository = stationRepository;
    //}

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

    public ResultSet findAllWays() throws SQLException {
        //Station objStationFirst = stationRepository.findByName(stationFirst);
        //Station objStationLast = stationRepository.findByName(stationLast);
        String sqlGetSheduleByStations =
                String.format("SELECT sched.id, s1.name, s2.name, stops1.stops1.time_end, stops2.time_begining FROM schedule sched" +
                        " LEFT JOIN stops stops1 ON sched.id = stops1.id_schedule" +
                        " LEFT JOIN stations s1 ON stops1.id_station = s1.id" +
                        " LEFT JOIN stops stops2 ON sched.id = stops2.id_schedule" +
                        " LEFT JOIN stations s2 ON stops2.id_station = s2.id" +
                        " WHERE s1.name = '%s' && s2.name = '%s' && stops1.time_begining < stops2.time_begining", stationFirst, stationLast);
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sqlGetSheduleByStations);
        return rs;
    }
}
