package ar.org.blb.login.administration.services;

import ar.org.blb.login.administration.mail.MailContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private JavaMailSender mailSender;
    private MailContentBuilder mailContentBuilder;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, MailContentBuilder mailContentBuilder) {
        this.mailSender = javaMailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    public void sendMail(String to, String subject, String templateName, Map<String, String> message, boolean isMultipart, boolean isHtml) {
        try {
            String body = mailContentBuilder.build(message, templateName);

            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, isMultipart, "UTF-8");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setText(body, isHtml);

            this.mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
