package com.trains.tickets.controller;

import com.trains.tickets.dto.StopsForMainDTO;
import com.trains.tickets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@RestController
public class TicketsRestController {
    @Value("${spring.datasource.url}")
    private String urlSql;
    @Value("${spring.datasource.username}")
    private String usernameSql;
    @Value("${spring.datasource.password}")
    private String passwordSql;

    private final UserRepository userRepository;

    public TicketsRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTableTickets")
    public Set<StopsForMainDTO> getTableTickets(
            @RequestParam String stationFirst,
            @RequestParam String stationLast
            ){
        String sqlGetSheduleByStations =
                String.format("SELECT sched.id, s1.name, s2.name, stops1.time_end, stops2.time_begining FROM schedule sched" +
                        " LEFT JOIN stops stops1 ON sched.id = stops1.id_schedule" +
                        " LEFT JOIN stations s1 ON stops1.id_station = s1.id" +
                        " LEFT JOIN stops stops2 ON sched.id = stops2.id_schedule" +
                        " LEFT JOIN stations s2 ON stops2.id_station = s2.id" +
                        " WHERE s1.name = '%s' && s2.name = '%s' && stops1.time_begining < stops2.time_begining", stationFirst, stationLast);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(urlSql, usernameSql, passwordSql);
            Set<StopsForMainDTO> stopsForMainDTOS = new HashSet<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlGetSheduleByStations);
            int columns = rs.getMetaData().getColumnCount();
            while(rs.next()){
                StopsForMainDTO stopForMainDTO = new StopsForMainDTO();
                stopForMainDTO.setStationFirst(rs.getString(2));
                stopForMainDTO.setStationLast(rs.getString(3));
                stopForMainDTO.setTimeDeparture(rs.getString(4));
                stopForMainDTO.setTimeArrival(rs.getString(5));
                stopsForMainDTOS.add(stopForMainDTO);
            }
            conn.close();
            return stopsForMainDTOS;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
