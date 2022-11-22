package com.trains.tickets.repository;

import com.trains.tickets.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends CrudRepository<Role, Long> {
    //Set<Role> findByName(String name);
    Role findByName(String name);
}
