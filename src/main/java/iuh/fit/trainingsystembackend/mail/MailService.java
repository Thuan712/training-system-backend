package iuh.fit.trainingsystembackend.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {
    @Autowired
    public JavaMailSender emailSender;

    public MailService() {
        this.emailSender = new MailConfig().getJavaMailSender();
    }

    public void sendEmail(String receiverEmail, String subject, String content) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(receiverEmail);
        message.setSubject(subject);
        message.setText(content);

        this.emailSender.send(message);
    }
}
