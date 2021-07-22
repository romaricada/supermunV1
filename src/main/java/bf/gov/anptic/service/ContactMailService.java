package bf.gov.anptic.service;

import bf.gov.anptic.service.dto.MailDTO;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class ContactMailService {
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;


    public ContactMailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
                       MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String subject, String content, boolean isMultipart, boolean isHtml) {
        String[] emails = {"applications@tic.gov.bf"};
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setBcc(emails);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
        }  catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", e);
        }
    }

    @Async
    public void sendEmailFromTemplate(MailDTO mailDTO, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable("contact", mailDTO);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, Locale.FRENCH);
        sendEmail(subject, content, false, true);
    }

    @Async
    public void sendRequestInformationMail(MailDTO user) {
        log.debug("Sending request for informations from '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/contactMail", "email.contact.title");
    }
}
