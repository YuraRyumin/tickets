package com.trains.tickets.repository;

import com.trains.tickets.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface StationRepository extends JpaRepository<Station, Long> {
    Station findByName(String name);
    Station findById(Integer id);

}
