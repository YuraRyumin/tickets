package com.trains.tickets.repository;

import com.trains.tickets.domain.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, Long> {

    Train findById(Integer id);

}
