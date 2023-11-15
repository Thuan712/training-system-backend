package com.thinkvitals.rabbitmq;

import com.thinkvitals.mail.MailEnvelope;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Value("${luxury-mall.rabbitmq.exchange.notification.dashboard}")
	private String dashboardExchange;

	@Value("${luxury-mall.rabbitmq.routingkey.notification.dashboard}")
	private String dashboardRoutingKey;

	@Value("${luxury-mall.rabbitmq.exchange.notification.user}")
	private String userExchange;

	@Value("${luxury-mall.rabbitmq.routingkey.notification.user}")
	private String userRoutingKey;

	@Value("${luxury-mall.rabbitmq.exchange.chat}")
	private String chatExchange;

	@Value("${luxury-mall.rabbitmq.routingkey.chat}")
	private String chatRoutingKey;

	@Value("${luxury-mall.rabbitmq.exchange.mail}")
	private String mailExchange;

	@Value("${luxury-mall.rabbitmq.routingkey.mail}")
	private String mailRoutingKey;

	public void sendDashBoardNotification(MessagePayload data) {amqpTemplate.convertAndSend(dashboardExchange, dashboardRoutingKey, data);}
	public void sendUserNotification(MessagePayload data) {amqpTemplate.convertAndSend(userExchange, userRoutingKey, data);}
	public void sendChatNotification(MessagePayload data) {amqpTemplate.convertAndSend(chatExchange, chatRoutingKey, data);}

	public void sendMail(MailEnvelope data) {amqpTemplate.convertAndSend(mailExchange, mailRoutingKey, data);}
}