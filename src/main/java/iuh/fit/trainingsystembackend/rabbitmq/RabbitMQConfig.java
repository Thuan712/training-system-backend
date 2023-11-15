package iuh.fit.trainingsystembackend.rabbitmq;

import org.springframework.amqp.core.*;
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
	@Value("${training-system.rabbitmq.queue.notification.dashboard}")
	String dashboardQueue;

	@Value("${training-system.rabbitmq.exchange.notification.dashboard}")
	String dashboardExchange;

	@Value("${training-system.rabbitmq.routingkey.notification.dashboard}")
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
	@Value("${training-system.rabbitmq.queue.notification.user}")
	String userQueue;

	@Value("${training-system.rabbitmq.exchange.notification.user}")
	String userExchange;

	@Value("${training-system.rabbitmq.routingkey.notification.user}")
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


	/* Declare Dead Letter Queue */
	@Value("${training-system.rabbitmq.deadletterexchange}")
	String deadLetterExchange;

	@Value("${training-system.rabbitmq.deadletterqueue}")
	String deadLetterQueue;

	@Value("${training-system.rabbitmq.deadletterroutingkey}")
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
