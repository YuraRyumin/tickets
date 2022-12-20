package com.trains.tickets.repository;

import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Set;

public interface StopRepository extends JpaRepository<Stop, Long> {

    Stop findById(Integer id);
    Set<Stop> findAllByStation(Station station);
    Set<Stop> findAllByStationName(String name);
    Set<Stop> findAllByStationNameAndTimeEnd(String name, LocalTime timeEnd);
    Set<Stop> findAllByStationNameAndTimeBegining(String name, LocalTime timeBegining);

}
