package com.trains.tickets.service;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Train;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.DaysOfWeekDTO;
import com.trains.tickets.dto.ScheduleDTO;
import com.trains.tickets.repository.ScheduleRepository;
import com.trains.tickets.repository.TrainRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Slf4j
@Transactional(readOnly = true)
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
        scheduleDTO.setDayOfWeek(returnNameOfDay(schedule.getDayOfWeek()));
        scheduleDTO.setName(schedule.getTrain().getNumber() + "_->_" + schedule.getTime());

        return scheduleDTO;
    }

    public ScheduleDTO getEmptyDto(){
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setId(0);
        scheduleDTO.setTime("");
        scheduleDTO.setTrain("");
        scheduleDTO.setDayOfWeek("");

        return scheduleDTO;
    }
    @Transactional
    public void saveSchedule(String time,
                             Integer dayOfWeek,
                             String train,
                             Integer scheduleId,
                             User user){
        if (scheduleId.equals(0)) {
            Train trainNew = trainRepository.findByNumber(train);
            if(trainNew != null){
                Schedule scheduleChanged = new Schedule(
                        time,
                        dayOfWeek,
                        trainNew
                );
                scheduleRepository.save(scheduleChanged);
                log.info(LocalDateTime.now().toString() + " - " + user.getLogin() + " create new schedule with id " +
                        scheduleChanged.getId() + " (" +
                        //scheduleChanged.getTrain().getNumber() + "; " +
                        scheduleChanged.getTime().toString() + "; " +
                        scheduleChanged.getDayOfWeek() + ")");
            }
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
            if (!scheduleChanged.getTrain().equals(trainNew) && trainNew != null) {
                scheduleChanged.setTrain(trainNew);
                wasChanged = true;
            }
            if (wasChanged){
                scheduleRepository.save(scheduleChanged);
                log.info(LocalDateTime.now().toString() + " - " + user.getLogin() + " change schedule with id " +
                        scheduleChanged.getId() + " (" +
                        //scheduleChanged.getTrain().getNumber() + "; " +
                        scheduleChanged.getTime().toString() + "; " +
                        scheduleChanged.getDayOfWeek() + ")");
            }
        }
    }

    public void putInfoAboutScheduleToModel(String schedule, Model model){
        if (schedule.equals("new")) {
            model.addAttribute("schedule", getEmptyDto());
            model.addAttribute("trains", trainService.convertAllEntityToDto(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))));
            model.addAttribute("daysOfWeek", returnDaysOfWeek(0));
        } else {
            Schedule selectedSchedule = scheduleRepository.findById(Integer.parseInt(schedule));
            if(selectedSchedule == null){
                throw  new NullPointerException("Schedule not found!");
            }
            model.addAttribute("schedule", convertEntityToDto(selectedSchedule));
            model.addAttribute("trains", trainService.convertAllEntityToDtoWithSelected(trainRepository.findAll(Sort.by(Sort.Direction.ASC, "number")), selectedSchedule.getTrain()));
            model.addAttribute("daysOfWeek", returnDaysOfWeek(selectedSchedule.getDayOfWeek()));
        }
    }

    public Set<DaysOfWeekDTO> returnDaysOfWeek(Integer selectedID){
        Set<DaysOfWeekDTO> daysOfWeekDTOSet = new HashSet<>();

        daysOfWeekDTOSet.add(returnOneDayDTO(2, "Monday", selectedID));
        daysOfWeekDTOSet.add(returnOneDayDTO(3, "Tuesday", selectedID));
        daysOfWeekDTOSet.add(returnOneDayDTO(4, "Wednesday", selectedID));
        daysOfWeekDTOSet.add(returnOneDayDTO(5, "Thursday", selectedID));
        daysOfWeekDTOSet.add(returnOneDayDTO(6, "Friday", selectedID));
        daysOfWeekDTOSet.add(returnOneDayDTO(7, "Saturday", selectedID));
        daysOfWeekDTOSet.add(returnOneDayDTO(1, "Sunday", selectedID));

        return daysOfWeekDTOSet;
    }

    private DaysOfWeekDTO returnOneDayDTO(Integer id, String name, Integer selectedID){
        DaysOfWeekDTO daysOfWeekDTO = new DaysOfWeekDTO();
        daysOfWeekDTO.setId(id);
        daysOfWeekDTO.setName(name);
        if(id.equals(selectedID)) {
            daysOfWeekDTO.setSelected(true);
        } else {
            daysOfWeekDTO.setSelected(false);
        }
        return daysOfWeekDTO;
    }

    private String returnNameOfDay(Integer number){
        String nameOfDay = "";
        switch (number){
            case 2: nameOfDay = "Monday";
            break;
            case 3: nameOfDay =  "Tuesday";
            break;
            case 4: nameOfDay =  "Wednesday";
            break;
            case 5: nameOfDay =  "Thursday";
            break;
            case 6: nameOfDay =  "Friday";
            break;
            case 7: nameOfDay =  "Saturday";
            break;
            case 1: nameOfDay =  "Sunday";
            break;
        }
        return nameOfDay;
    }
}
