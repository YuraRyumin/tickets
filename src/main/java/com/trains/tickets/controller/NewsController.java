package com.trains.tickets.controller;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.NewsRepository;
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
    private final UserService userService;

    public NewsController(NewsRepository newsRepository, NewsService newsService, UserService userService) {
        this.newsRepository = newsRepository;
        this.newsService = newsService;
        this.userService = userService;
    }

    @GetMapping
    public String newsList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("news", newsService.convertAllEntityToDto(newsRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))));
        if(user != null) {
            model.addAttribute("user", userService.convertEntityToDtoForNav(user));
            if (user.isAdmin()) {
                model.addAttribute("adminRole", true);
            }
            if (user.isOperator()) {
                model.addAttribute("operatorRole", true);
            }
        }
        return "news";
    }
}
