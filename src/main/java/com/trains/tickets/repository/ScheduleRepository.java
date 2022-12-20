package com.trains.tickets.repository;

import com.trains.tickets.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Schedule findById(Integer id);
    Schedule findByTimeAndTrainNumber(String time, String number);

}
