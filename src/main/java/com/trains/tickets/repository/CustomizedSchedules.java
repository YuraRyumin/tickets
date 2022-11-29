package com.trains.tickets.repository;

import com.trains.tickets.dto.StopsForMainDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomizedSchedules extends CrudRepository<StopsForMainDTO, Long> {
    List<com.trains.tickets.dto.StopsForMainDTO> getRoutesByStationNames(String stationFirst, String stationLast);
}
