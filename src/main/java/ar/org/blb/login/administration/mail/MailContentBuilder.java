package ar.org.blb.login.administration.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(Map<String, String> message, String templateName) {
        Context context = new Context();

        for (String keys : message.keySet()) {
            context.setVariable(keys, message.get(keys));
        }

        return templateEngine.process(templateName, context);
    }
}
