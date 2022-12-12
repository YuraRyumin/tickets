package com.trains.tickets.service;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.NewsRepository;
import com.trains.tickets.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class PrivatePageService {
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final NewsRepository newsRepository;
    private final NewsService newsService;

    public PrivatePageService(TicketRepository ticketRepository, TicketService ticketService, NewsRepository newsRepository, NewsService newsService) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.newsRepository = newsRepository;
        this.newsService = newsService;
    }

    public void putInfoAboutPrivatePageToModel(User user, Model model, String uuid){
        model.addAttribute("uuid", uuid);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("telephone", user.getTelephone());
        if(user.getRole().equals(null)){
            model.addAttribute("role", "");
        } else {
            model.addAttribute("role", user.getRole().getName());
        }
        if(user.getPassenger().equals(null)){
            model.addAttribute("passenger", "");
        } else {
            model.addAttribute("passenger", user.getPassenger().getName() + " " + user.getPassenger().getSurname());
        }
        model.addAttribute("tickets", ticketService.convertAllEntityToDto(ticketRepository.findAllByUserLogin(user.getLogin())));
        model.addAttribute("news", newsService.convertAllEntityToDto(newsRepository.findAllByUserUuid(user.getUuid())));
    }
}
