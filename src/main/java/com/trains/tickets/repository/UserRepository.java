package com.trains.tickets.repository;

import com.trains.tickets.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    User findById(Integer id);
    User findByActivationCode(String code);
    User findByUuid(String uuid);
}
