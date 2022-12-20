package com.trains.tickets.repository;

import com.trains.tickets.domain.Wagon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface WagonRepository extends JpaRepository<Wagon, Long> {

    Wagon findById(Integer id);
    Wagon findByName(String name);
    Set<Wagon> findAllByTrainId(Integer trainId);
    Set<Wagon> findAllByTrainNumber(String trainNumber);

}
