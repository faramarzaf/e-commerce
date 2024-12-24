package org.example.ecommerce.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderMessageSender {

    private final JmsTemplate jmsTemplate;

    public void sendOrderMessage(String queueName, String message) {
        jmsTemplate.convertAndSend(queueName, message);
        log.info("Message sent to queue: {}", queueName);
    }
}
