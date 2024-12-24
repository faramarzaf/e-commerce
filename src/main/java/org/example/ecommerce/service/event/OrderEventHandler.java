package org.example.ecommerce.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class OrderEventHandler {

    private final OrderMessageSender orderMessageSender;

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderPlaced(Long orderId) {
        orderMessageSender.sendOrderMessage("order.queue", "Order ID: " + orderId);
    }

}
