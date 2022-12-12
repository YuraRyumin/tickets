package com.trains.tickets.service;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Train;
import com.trains.tickets.dto.ScheduleDTO;
import com.trains.tickets.repository.ScheduleRepository;
import com.trains.tickets.repository.TrainRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TrainRepository trainRepository;
    private final TrainService trainService;

    public ScheduleService(ScheduleRepository scheduleRepository, TrainRepository trainRepository, TrainService trainService) {
        this.scheduleRepository = scheduleRepository;
        this.trainRepository = trainRepository;
        this.trainService = trainService;
    }

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

    public void saveSchedule(String time,
                              Integer dayOfWeek,
                              String train,
                              Integer scheduleId){
        if (scheduleId.equals(0)) {
            Schedule scheduleChanged = new Schedule(
                    time,
                    dayOfWeek,
                    trainRepository.findByNumber(train)
            );
            scheduleRepository.save(scheduleChanged);
        } else {
            Schedule scheduleChanged = scheduleRepository.findById(scheduleId);
            boolean wasChanged = false;
            if (!scheduleChanged.getTime().equals(time)) {
                scheduleChanged.setTime(time);
                wasChanged = true;
            }
            if (!scheduleChanged.getDayOfWeek().equals(dayOfWeek)) {
                scheduleChanged.setDayOfWeek(dayOfWeek);
                wasChanged = true;
            }
            Train trainNew = trainRepository.findByNumber(train);
            if (!scheduleChanged.getTrain().equals(trainNew)) {
                scheduleChanged.setTrain(trainNew);
                wasChanged = true;
            }
            if (wasChanged){
                scheduleRepository.save(scheduleChanged);
            }
        }
    }

    public void putInfoAboutScheduleToModel(String schedule, Model model){
        if (schedule.equals("new")) {
            model.addAttribute("schedule", getEmptyDto());
            model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
        } else {
            Schedule selectedSchedule = scheduleRepository.findById(Integer.parseInt(schedule));
            if(selectedSchedule == null){
                throw  new NullPointerException("Schedule not found!");
            }
            model.addAttribute("schedule", convertEntityToDto(selectedSchedule));
            model.addAttribute("trains", trainService.convertAllEntityToDtoWithSelected(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number")), selectedSchedule.getTrain()));
        }
    }
}
