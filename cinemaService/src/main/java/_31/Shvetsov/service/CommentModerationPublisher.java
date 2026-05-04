package _31.Shvetsov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentModerationPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.comments.exchange}")
    private String exchange;

    @Value("${rabbitmq.comments.routing-key}")
    private String routingKey;

    public boolean publish(Integer commentId) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, commentId.toString());
            return true;
        } catch (AmqpException exception) {
            return false;
        }
    }
}
