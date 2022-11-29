package com.trains.tickets.repository;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.projection.StopsTwoTrainsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface TwoTrainsDTORepository extends JpaRepository<Schedule, Long> {

}
