package com.trains.tickets.repository;

import com.trains.tickets.domain.ServiceClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceClassRepository extends JpaRepository<ServiceClass, Long> {

    ServiceClass findById(Integer id);
    ServiceClass findByName(String name);

}
