package com.trains.tickets.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

class MailSenderTest {
    @Mock
    SimpleMailMessage simpleMailMessage;

    @Mock
    MailSender mailSenderMock;

    @InjectMocks
    MailSender mailSenderInj;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSend(){
        //Assertions.assertDoesNotThrow(() -> mailSender.send("test@mail.ru", "AAA", "BBB"));
    }
}