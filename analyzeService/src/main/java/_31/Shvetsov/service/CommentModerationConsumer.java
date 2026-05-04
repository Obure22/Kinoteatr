package _31.Shvetsov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentModerationConsumer {

    private final CommentAnalyzeService commentAnalyzeService;

    @RabbitListener(queues = "${rabbitmq.comments.queue}", concurrency = "${rabbitmq.comments.concurrency:5}")
    public void consume(String commentId) {
        commentAnalyzeService.moderate(Integer.valueOf(commentId));
    }
}
