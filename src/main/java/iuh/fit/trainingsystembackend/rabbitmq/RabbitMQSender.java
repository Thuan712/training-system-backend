package iuh.fit.trainingsystembackend.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${training-system.rabbitmq.exchange.notification.dashboard}")
    private String dashboardExchange;

    @Value("${training-system.rabbitmq.routingkey.notification.dashboard}")
    private String dashboardRoutingKey;

    @Value("${training-system.rabbitmq.exchange.notification.user}")
    private String userExchange;

    @Value("${training-system.rabbitmq.routingkey.notification.user}")
    private String userRoutingKey;

    public void sendDashBoardNotification(MessagePayload data) {
        amqpTemplate.convertAndSend(dashboardExchange, dashboardRoutingKey, data);
    }

    public void sendUserNotification(MessagePayload data) {
        amqpTemplate.convertAndSend(userExchange, userRoutingKey, data);
    }

}