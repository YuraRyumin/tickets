package com.trains.tickets.service;

import com.trains.tickets.dto.StopsForMainDTO;
import com.trains.tickets.repository.CustomizedSchedules;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class CustomizedSchedulesImpl implements CustomizedSchedules {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<StopsForMainDTO> getRoutesByStationNames(String stationFirst, String stationLast) {
        return em.createQuery(String.format("SELECT sched.id, s1.name, s2.name, stops1.time_end, stops2.time_begining FROM schedule sched" +
                        " LEFT JOIN stops stops1 ON sched.id = stops1.id_schedule" +
                        " LEFT JOIN stations s1 ON stops1.id_station = s1.id" +
                        " LEFT JOIN stops stops2 ON sched.id = stops2.id_schedule" +
                        " LEFT JOIN stations s2 ON stops2.id_station = s2.id" +
                        " WHERE s1.name = '%s' && s2.name = '%s' && stops1.time_begining < stops2.time_begining", stationFirst, stationLast))
                .getResultList();
    }
}
