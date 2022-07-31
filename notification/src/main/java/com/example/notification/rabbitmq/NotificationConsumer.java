package com.example.notification.rabbitmq;

import com.example.notification.dto.TransactionMailRequest;
import com.example.notification.dto.UserMailRequest;
import com.example.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(UserMailRequest userMailRequest){
        log.info("consumed {} from queue", userMailRequest);
        notificationService.sendVerificationEmail(userMailRequest);

    }


    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(TransactionMailRequest transactionMailRequest){
        log.info("consumed {} from queue", transactionMailRequest.toString());
        notificationService.sendTransactionEmail(transactionMailRequest);

    }
}
