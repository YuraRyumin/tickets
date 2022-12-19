package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.NewsRepository;
import com.trains.tickets.service.MainService;
import com.trains.tickets.service.NewsService;
import com.trains.tickets.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/news")
public class NewsController {
    private final NewsRepository newsRepository;
    private final NewsService newsService;
    private final MainService mainService;

    public NewsController(NewsRepository newsRepository, NewsService newsService, MainService mainService) {
        this.newsRepository = newsRepository;
        this.newsService = newsService;
        this.mainService = mainService;
    }

    @GetMapping
    public String newsList(@AuthenticationPrincipal User user,
                           Model model){
        mainService.putUserInfoToModel(user, model);
        model.addAttribute("news", newsService.convertAllEntityToDto(newsRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))));
        return "allNews";
    }
}
