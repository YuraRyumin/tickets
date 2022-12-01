package com.trains.tickets.repository;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Passenger findByPassport(String passport);
    Passenger findById(Integer id);
}
