package com.trains.tickets.repository;

import com.trains.tickets.domain.Distance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistanceRepository extends JpaRepository<Distance, Long> {
    Distance findById(Integer id);
}
