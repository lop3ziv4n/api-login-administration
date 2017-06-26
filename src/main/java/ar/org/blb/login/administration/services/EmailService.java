package ar.org.blb.login.administration.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMailPlainText(String to, String subject, String template, Map<String, Object> message) {
        this.send(to, subject, this.build(template, message), false);
    }

    public void sendMailHtml(String to, String subject, String template, Map<String, Object> message) {
        this.send(to, subject, this.build(template, message), true);
    }

    private void send(String to, String subject, String message, boolean html) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom(this.from);
            helper.setSubject(subject);
            helper.setText(message, html);
            javaMailSender.send(mail);
        } catch (Exception e) {
            LOGGER.error(String.format("Problem with sending email to: {}, error message: {}", to, e.getMessage()));
        }
    }

    private String build(String template, Map<String, Object> message) {
        Context context = new Context();

        for (String keys : message.keySet()) {
            context.setVariable(keys, message.get(keys));
        }

        return this.templateEngine.process(template, context);
    }
}
