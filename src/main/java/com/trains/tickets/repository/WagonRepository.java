package com.trains.tickets.repository;

import com.trains.tickets.domain.Wagon;
import com.trains.tickets.projection.SeatsProjection;
import com.trains.tickets.projection.WagonInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface WagonRepository extends JpaRepository<Wagon, Long> {

    Wagon findById(Integer id);
    Wagon findByName(String name);
    Set<Wagon> findAllByTrainId(Integer trainId);

}
