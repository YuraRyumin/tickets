package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.NewsRepository;
import com.trains.tickets.repository.PassengerRepository;
import com.trains.tickets.repository.TicketRepository;
import com.trains.tickets.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Transactional(readOnly = true)
@Service
public class PrivatePageService {
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final NewsRepository newsRepository;
    private final NewsService newsService;
    private final PassengerRepository passengerRepository;
    private final PassengerService passengerService;
    private final UserRepository userRepository;

    public PrivatePageService(TicketRepository ticketRepository, TicketService ticketService, NewsRepository newsRepository, NewsService newsService, PassengerRepository passengerRepository, PassengerService passengerService, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.newsRepository = newsRepository;
        this.newsService = newsService;
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
        this.userRepository = userRepository;
    }

    public void putInfoAboutPrivatePageToModel(User user, Model model, String uuid){
        model.addAttribute("uuid", uuid);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("telephone", user.getTelephone());
        if(user.getRole() == null){
            model.addAttribute("role", "");
        } else {
            model.addAttribute("role", user.getRole().getName());
        }
        if(user.getPassenger() == null){
            model.addAttribute("passenger", "");
        } else {
            model.addAttribute("passenger", user.getPassenger().getName() + " " + user.getPassenger().getSurname());
        }
        model.addAttribute("tickets", ticketService.convertAllEntityToDto(ticketRepository.findAllByUserLogin(user.getLogin())));
        model.addAttribute("news", newsService.convertAllEntityToDto(newsRepository.findAllByUserUuid(user.getUuid())));
        if(user.getPassenger() == null){
            model.addAttribute("passengerInfo", passengerService.getEmptyDto());
        } else {
            model.addAttribute("passengerInfo", passengerService.convertEntityToDto(user.getPassenger()));
        }
        model.addAttribute("genders", passengerService.getGendersList(user.getPassenger()));
    }
    @Transactional
    public void saveInfoAboutPassenger(User user,
                                       String passengerName,
                                       String passengerSurname,
                                       String passengerPassport,
                                       String passengerGender,
                                       String passengerDateOfBirth){
        Passenger passenger = user.getPassenger();

        String[] fullTimeDOB = passengerDateOfBirth.split("-");
        Integer dayOfDOB = Integer.valueOf(fullTimeDOB[2]);
        Integer monthOfDOB = Integer.valueOf(fullTimeDOB[1]);
        Integer yearOfDOB = Integer.valueOf(fullTimeDOB[0]);
        LocalDate localDateDOB = LocalDate.of(yearOfDOB, monthOfDOB, dayOfDOB);
        if(passenger == null){
            passenger = new Passenger(passengerName, passengerSurname, passengerPassport, passengerGender, localDateDOB);
            passengerRepository.save(passenger);
            user.setPassenger(passenger);
            userRepository.save(user);
        } else {
            boolean wasChanged = false;
            if(!passenger.getName().equals(passengerName)){
                passenger.setName(passengerName);
                wasChanged = true;
            }
            if(!passenger.getSurname().equals(passengerSurname)){
                passenger.setSurname(passengerSurname);
                wasChanged = true;
            }
            if(!passenger.getPassport().equals(passengerPassport)){
                passenger.setPassport(passengerPassport);
                wasChanged = true;
            }
            if(!passenger.getGender().equals(passengerGender)){
                passenger.setGender(passengerGender);
                wasChanged = true;
            }
            if(!passenger.getDateOfBirth().equals(localDateDOB)){
                passenger.setDateOfBirth(localDateDOB);
                wasChanged = true;
            }
            if(wasChanged){
                passengerRepository.save(passenger);
            }
        }
    }
}
