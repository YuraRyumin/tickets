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

    @Query(value = "SELECT wagons.id id, wagons.name name, wagons.seats seats FROM wagons WHERE wagons.id_train = ?1", nativeQuery = true)
    Set<WagonInfoProjection> findByTrainID(Integer trainId);

    @Query(value = "SELECT tickets.seat seat, wagons.seats maxSeats  FROM tickets LEFT JOIN wagons on tickets.id_wagons = wagons.id LEFT JOIN schedule on tickets.id_schedule = schedule.id WHERE schedule.time = ?1 && tickets.id_wagons = ?2 && tickets.date_ticket = ?3", nativeQuery = true)
    Set<SeatsProjection> findSeatsByTrainAndSchedule(String schedule, Integer wagonID, String dateTicket);
}
