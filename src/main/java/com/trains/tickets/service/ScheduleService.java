package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Schedule;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.dto.ScheduleDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScheduleService {
    public Iterable<ScheduleDTO> convertAllEntityToDto(Iterable<Schedule> schedules){
        return StreamSupport.stream(schedules.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Iterable<ScheduleDTO> convertAllEntityToDtoWithSelected(Iterable<Schedule> schedules, Schedule selectedSchedule){
        return StreamSupport.stream(schedules.spliterator(), false)
                .map(schedule -> {
                    ScheduleDTO scheduleDTO = convertEntityToDto(schedule);
                    if (schedule.getTime().equals(selectedSchedule.getTime())){
                        scheduleDTO.setSelected(true);
                    } else {
                        scheduleDTO.setSelected(false);
                    }
                    return scheduleDTO;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ScheduleDTO convertEntityToDto(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setTime(schedule.getTime());
        scheduleDTO.setTrain(schedule.getTrain().getNumber());
        scheduleDTO.setDayOfWeek(schedule.getDayOfWeek());

        return scheduleDTO;
    }

    public ScheduleDTO getEmptyDto(){
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setId(0);
        scheduleDTO.setTime("");
        scheduleDTO.setTrain("");
        scheduleDTO.setDayOfWeek(0);

        return scheduleDTO;
    }
}
