package com.trains.tickets.controller;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.StopsForMainDTO;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.UserRepository;
import com.trains.tickets.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class TicketsSearchController {
    @Value("${spring.datasource.url}")
    private String urlSql;
    @Value("${spring.datasource.username}")
    private String usernameSql;
    @Value("${spring.datasource.password}")
    private String passwordSql;
    @Value("${spring.mail.username}")
    private String username;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    @Autowired
    private final StationService stationService;

    public TicketsSearchController(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, StationRepository stationRepository, StationService stationService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    @GetMapping("/ticketsSearch")
    public String main(@AuthenticationPrincipal User user,
                       Map<String, Object> model){
        Iterable<Role> role = roleRepository.findAll();
        Iterable<Station> stations = stationRepository.findAll();
        model.put("roles", role);
        model.put("user", user);
        model.put("stations", stationService.convertEntityToDto(stations));
        if(user.isAdmin()) {
            model.put("adminRole", true);
        }
        return "ticketsSearch";
    }

    @GetMapping("chooseTickets")
    public String chooseTickets(@AuthenticationPrincipal User user,
                                @RequestParam String stationFirst,
                                @RequestParam String stationLast,
                                Map<String, Object> model){
//        String sqlGetSheduleByStations =
//                String.format("SELECT sched.id, s1.name, s2.name, stops1.time_end, stops2.time_begining FROM schedule sched" +
//                        " LEFT JOIN stops stops1 ON sched.id = stops1.id_schedule" +
//                        " LEFT JOIN stations s1 ON stops1.id_station = s1.id" +
//                        " LEFT JOIN stops stops2 ON sched.id = stops2.id_schedule" +
//                        " LEFT JOIN stations s2 ON stops2.id_station = s2.id" +
//                        " WHERE s1.name = '%s' && s2.name = '%s' && stops1.time_begining < stops2.time_begining", stationFirst, stationLast);
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(urlSql, usernameSql, passwordSql);
//            Set<StopsForMainDTO> stopsForMainDTOS = new HashSet<>();
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(sqlGetSheduleByStations);
//            int columns = rs.getMetaData().getColumnCount();
//            while(rs.next()){
//                StopsForMainDTO stopForMainDTO = new StopsForMainDTO();
//                stopForMainDTO.setStationFirst(rs.getString(2));
//                stopForMainDTO.setStationLast(rs.getString(3));
//                stopForMainDTO.setTimeDeparture(rs.getString(4));
//                stopForMainDTO.setTimeArrival(rs.getString(5));
//                stopsForMainDTOS.add(stopForMainDTO);
//            }
//            conn.close();
//            model.put("user", user);
//            Iterable<Station> stations = stationRepository.findAll();
//            model.put("stations", stationService.convertEntityToDto(stations));
//            model.put("stopsForMainDTOS", stopsForMainDTOS);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return "main";
    }



    //@GetMapping("/getTableTickets")
    //public String getTableTickets(){
    //    return userRepository.findByLogin("admin");
    //}
}
