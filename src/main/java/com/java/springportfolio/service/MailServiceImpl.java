package com.java.springportfolio.service;

import com.java.springportfolio.entity.NotificationEmail;
import com.java.springportfolio.exception.PortfolioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    @Override
    public void sendMail(NotificationEmail notificationEmail) {
        log.info("Sending an email to: '{}'", notificationEmail.getRecipient());
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("my_portfolio.tk");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };
        try {
            mailSender.send(messagePreparator);
            log.info("The notification email has been successfully sent!");
        } catch (MailException e) {
            log.error("An exception occurred when sending mail to: '{}' ", notificationEmail.getRecipient());
            throw new PortfolioException("An exception occurred when sending mail to " + notificationEmail.getRecipient());
        }
    }
}
