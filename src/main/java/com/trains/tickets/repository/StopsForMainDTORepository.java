package com.trains.tickets.repository;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.projection.StopsDirectProjection;
import com.trains.tickets.projection.StopsTwoTrainsProjection;
import com.trains.tickets.projection.TicketInfoProjection;
import com.trains.tickets.projection.WagonInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface StopsForMainDTORepository extends JpaRepository<Schedule, Long> {
    @Query(value = "SELECT sched.id, stops1.nameFirst stationFirst, stops2.nameLast stationLast, stops1.time_end timeDeparture, stops2.time_begining timeArrival FROM schedule sched" +
            "    LEFT JOIN (SELECT stops.id_schedule, stops.time_begining , stops.time_end time_end, s1.name nameFirst FROM stops" +
            "        LEFT JOIN stations s1 ON stops.id_station = s1.id) stops1 ON sched.id = stops1.id_schedule" +
            "    LEFT JOIN (SELECT stops.id_schedule, stops.time_begining , stops.time_end, s2.name nameLast FROM  stops" +
            "        LEFT JOIN stations s2 ON stops.id_station = s2.id)  stops2 ON sched.id = stops2.id_schedule" +
            " WHERE stops1.nameFirst = ?1 && stops2.nameLast = ?2 && sched.day_of_week = ?3 && stops1.time_end > ?4 && stops1.time_begining < stops2.time_begining",
            nativeQuery = true)
    Set<StopsDirectProjection> findDirectScheduleByTwoStations(String nameFirst, String nameLast, Integer dayOfWeek, String timeNowPlusTen);

    @Query(value = "SELECT TempFirst.time_end TimeBeginningFirstTrain, TempFirst.name NameFirstStationFirstTrain, stations.name NameSecondStationFirstTrain, stop1.time_begining TimeEndFirstTrain, stop2.time_end TimeBeginningSecondTrain, stations.name NameFirstStationSecondTrain, TempSecond.name NameSecondStationSecondTrain, TempSecond.time_begining TimeEndSecondTrain " +
            "FROM stations " +
            "LEFT JOIN stops stop1 ON stations.id = stop1.id_station " +
            "LEFT JOIN (SELECT sched.id, s1.name, stops1.time_begining, stops1.time_end, sched.day_of_week dayOfWeek " +
            "FROM schedule sched " +
            "LEFT JOIN stops stops1 ON sched.id = stops1.id_schedule " +
            "LEFT JOIN stations s1 ON stops1.id_station = s1.id) TempFirst ON stop1.id_schedule = TempFirst.id " +
            "LEFT JOIN stops stop2 ON stations.id = stop2.id_station " +
            "LEFT JOIN (SELECT sched.id, s1.name, stops1.time_begining, stops1.time_end, sched.day_of_week dayOfWeek " +
            "FROM schedule sched LEFT JOIN stops stops1 ON sched.id = stops1.id_schedule " +
            "LEFT JOIN stations s1 ON stops1.id_station = s1.id) TempSecond ON stop2.id_schedule = TempSecond.id " +
            "WHERE TempFirst.name = ?1 && TempSecond.name = ?2 && TempFirst.dayOfWeek = ?3 && TempSecond.dayOfWeek = ?4 && TempFirst.time_end > ?5 && stop1.time_begining < stop2.time_end",
            nativeQuery = true)
    Set<StopsTwoTrainsProjection> findScheduleByTwoStations(String firstStation, String lastStation, Integer dayOfWeekFirst, Integer dayOfWeekSecond, String timeNowPlusTen);

    @Query(value = "SELECT sched.id  scheduleID, trains.number + ' -> ' + sched.time schedName, sched.time schedule, stops1.nameFirst stationFirst, stops2.nameLast stationLast, stops1.time_end timeDeparture, stops2.time_begining timeArrival," +
            "       COALESCE(passengers.name, '') passengerName, COALESCE(passengers.surname, '') passengerSurname, passengers.gender passengerGender, COALESCE(passengers.passport, '') passengerPassport, COALESCE(passengers.date_of_birth, '') passengerDate, trains.number trainNumber, trains.id trainId FROM schedule sched" +
            "                LEFT JOIN (SELECT stops.id_schedule, stops.time_begining , stops.time_end, s1.name nameFirst FROM stops" +
            "                    LEFT JOIN stations s1 ON stops.id_station = s1.id) stops1 ON sched.id = stops1.id_schedule" +
            "                LEFT JOIN (SELECT stops.id_schedule, stops.time_begining, stops.time_end, s2.name nameLast FROM  stops" +
            "                    LEFT JOIN stations s2 ON stops.id_station = s2.id)  stops2 ON sched.id = stops2.id_schedule" +
            "                LEFT JOIN trains on trains.id = sched.id_train" +
            "                LEFT JOIN (SELECT passengers.name, passengers.surname, passengers.passport, passengers.date_of_birth, passengers.gender FROM  passengers" +
            "                           WHERE passengers.id = ?1)  passengers ON true" +
            "             WHERE stops1.nameFirst = ?2 && stops2.nameLast =  ?3 && stops1.time_end = ?4 && stops2.time_begining = ?5 && stops1.time_begining < stops2.time_begining", nativeQuery = true)
    Set<TicketInfoProjection> findTicketsInfoAndPassanger(Integer idPassenger, String firstStation, String lastStation,
                                                          String timeDeparture, String timeArrival);


}
