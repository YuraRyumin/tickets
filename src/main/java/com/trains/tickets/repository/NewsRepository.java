package com.trains.tickets.repository;

import com.trains.tickets.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface NewsRepository extends JpaRepository<News, Long> {
    News findById(Integer id);
    Set<News> findAllByUserUuid(String uuid);
}
