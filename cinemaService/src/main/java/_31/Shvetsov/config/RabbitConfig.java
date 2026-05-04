package _31.Shvetsov.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue commentModerationQueue(@Value("${rabbitmq.comments.queue}") String queue) {
        return new Queue(queue, true);
    }

    @Bean
    public DirectExchange commentModerationExchange(@Value("${rabbitmq.comments.exchange}") String exchange) {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    public Binding commentModerationBinding(Queue commentModerationQueue,
                                            DirectExchange commentModerationExchange,
                                            @Value("${rabbitmq.comments.routing-key}") String routingKey) {
        return BindingBuilder.bind(commentModerationQueue).to(commentModerationExchange).with(routingKey);
    }
}
