package com.trains.tickets.repository;

import com.trains.tickets.domain.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopRepository extends JpaRepository<Stop, Long> {

    Stop findById(Integer id);

}
