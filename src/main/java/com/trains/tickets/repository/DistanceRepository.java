package com.trains.tickets.repository;

import com.trains.tickets.domain.Distance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistanceRepository extends JpaRepository<Distance, Long> {
    Distance findById(Integer id);
}
