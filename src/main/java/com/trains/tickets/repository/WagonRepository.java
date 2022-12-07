package com.trains.tickets.repository;

import com.trains.tickets.domain.Wagon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WagonRepository extends JpaRepository<Wagon, Long> {

    Wagon findById(Integer id);
    Wagon findByName(String name);

}
