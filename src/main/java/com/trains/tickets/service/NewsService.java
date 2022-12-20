package com.trains.tickets.service;

import com.trains.tickets.domain.News;
import com.trains.tickets.dto.NewsDTO;
import com.trains.tickets.repository.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Transactional(readOnly = true)
@Service
public class NewsService {
    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Iterable<NewsDTO> convertAllEntityToDtoWithSelected(Iterable<News> news, News SelectedNews){
        return StreamSupport.stream(news.spliterator(), false)
                .map(newsOne -> {
                    NewsDTO newsDTO = convertEntityToDto(newsOne);
                    if (newsOne.getId().equals(SelectedNews.getId())) {
                        newsDTO.setSelected(true);
                    } else {
                        newsDTO.setSelected(false);
                    }
                    return newsDTO;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Iterable<NewsDTO> convertAllEntityToDto(Iterable<News> news){
        return StreamSupport.stream(news.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public NewsDTO convertEntityToDto(News news){
        NewsDTO newsDTO = new NewsDTO();

        newsDTO.setId(news.getId());
        newsDTO.setHeader(news.getHeader());
        newsDTO.setBody(news.getBody());
        newsDTO.setUser(news.getUser().getLogin());
        newsDTO.setDate(news.getDate());

        return newsDTO;
    }
}
