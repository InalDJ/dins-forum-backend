package com.java.springportfolio.service;

import com.java.springportfolio.entity.NotificationEmail;

public interface MailService {

    void sendMail(NotificationEmail notificationEmail);
}
