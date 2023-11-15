package com.thinkvitals.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	/* Declare Dashboard Notification Queue */
	@Value("${luxury-mall.rabbitmq.queue.notification.dashboard}")
	String dashboardQueue;

	@Value("${luxury-mall.rabbitmq.exchange.notification.dashboard}")
	String dashboardExchange;

	@Value("${luxury-mall.rabbitmq.routingkey.notification.dashboard}")
	String dashboardRoutingKey;

	@Bean
	DirectExchange dashboardExchange() {
		return new DirectExchange(dashboardExchange);
	}

	@Bean
	Binding dashboardBinding() {
		return BindingBuilder.bind(dashboardQueue()).to(dashboardExchange()).with(dashboardRoutingKey);
	}

	@Bean
	Queue dashboardQueue() {
		return QueueBuilder.durable(dashboardQueue)
				.withArgument("x-dead-letter-exchange", deadLetterExchange)
				.withArgument("x-dead-letter-routing-key", deadLetterRoutingKey).build();
	}
	/* End Dashboard Queue */

	/* Declare User Notification Queue */
	@Value("${luxury-mall.rabbitmq.queue.notification.user}")
	String userQueue;

	@Value("${luxury-mall.rabbitmq.exchange.notification.user}")
	String userExchange;

	@Value("${luxury-mall.rabbitmq.routingkey.notification.user}")
	String userRoutingKey;

	@Bean
	DirectExchange userExchange() {
		return new DirectExchange(userExchange);
	}

	@Bean
	Binding userBinding() {
		return BindingBuilder.bind(userQueue()).to(userExchange()).with(userRoutingKey);
	}

	@Bean
	Queue userQueue() {
		return QueueBuilder.durable(userQueue)
				.withArgument("x-dead-letter-exchange", deadLetterExchange)
				.withArgument("x-dead-letter-routing-key", deadLetterRoutingKey).build();
	}
	/* End Dashboard Queue */

	/* Declare Chat Queue */
	@Value("${luxury-mall.rabbitmq.queue.chat}")
	String chatQueue;

	@Value("${luxury-mall.rabbitmq.exchange.chat}")
	String chatExchange;

	@Value("${luxury-mall.rabbitmq.routingkey.chat}")
	String chatRoutingKey;

	@Bean
	DirectExchange chatExchange() {
		return new DirectExchange(chatExchange);
	}

	@Bean
	Binding chatBinding() {
		return BindingBuilder.bind(chatQueue()).to(chatExchange()).with(chatRoutingKey);
	}

	@Bean
	Queue chatQueue() {
		return QueueBuilder.durable(chatQueue)
				.withArgument("x-dead-letter-exchange", deadLetterExchange)
				.withArgument("x-dead-letter-routing-key", deadLetterRoutingKey).build();
	}

	/* End Chat Queue */

	/* Declare Mail Queue */
	@Value("${luxury-mall.rabbitmq.queue.mail}")
	String mailQueue;

	@Value("${luxury-mall.rabbitmq.exchange.mail}")
	String mailExchange;

	@Value("${luxury-mall.rabbitmq.routingkey.mail}")
	String mailRoutingKey;

	@Bean
	DirectExchange mailExchange() {
		return new DirectExchange(mailExchange);
	}

	@Bean
	Binding mailBinding() {
		return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(mailRoutingKey);
	}

	@Bean
	Queue mailQueue() {
		return QueueBuilder.durable(mailQueue)
				.withArgument("x-dead-letter-exchange", deadLetterExchange)
				.withArgument("x-dead-letter-routing-key", deadLetterRoutingKey).build();
	}
	/* End Mail Queue */

	/* Declare Dead Letter Queue */
	@Value("${luxury-mall.rabbitmq.deadletterexchange}")
	String deadLetterExchange;

	@Value("${luxury-mall.rabbitmq.deadletterqueue}")
	String deadLetterQueue;

	@Value("${luxury-mall.rabbitmq.deadletterroutingkey}")
	String deadLetterRoutingKey;

	@Bean
	Queue dlq() {
		return QueueBuilder.durable(deadLetterQueue).build();
	}

	@Bean
	DirectExchange deadLetterExchange() {
		return new DirectExchange(deadLetterExchange);
	}

	@Bean
	Binding DLQbinding() {
		return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with(deadLetterRoutingKey);
	}
	/* End Dead Letter Queue */

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
}
