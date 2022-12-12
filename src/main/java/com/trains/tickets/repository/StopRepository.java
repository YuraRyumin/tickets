package com.trains.tickets.repository;

import com.trains.tickets.domain.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface StopRepository extends JpaRepository<Stop, Long> {

    Stop findById(Integer id);
    Set<Stop> findAllByStationName(String name);

}
