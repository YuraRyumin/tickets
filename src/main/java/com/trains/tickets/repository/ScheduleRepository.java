package com.trains.tickets.repository;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.dto.StopsForMainDTO;
import com.trains.tickets.dto.TwoTrainsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Schedule findById(Integer id);
    Schedule findByTime(String time);
    Schedule findByTimeAndTrainNumber(String time, String number);
    //Set<Schedule> findAllByStationName(String name);
    //findDirectScheduleByTwoStations
    //Set<Schedule> findAllByStationName(String name);

}
