package iuh.fit.trainingsystembackend.mail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mail {
    private String from;
    private List<String> cc;
    private List<String> bcc;
    private List<String> tos;
    private String replyTo;
    private String to;
    private String subject;
    private Map<String, Object> data;
    private String template;
    private Date sendDate = new Date();
}
