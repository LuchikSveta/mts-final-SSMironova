package ru.mts.siebel.delivery.kafka.consumer;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mts.siebel.delivery.service.DeliveryOptyService;
import ru.mts.siebel.model.DeliveryMessage;

@Log4j2
@Service
public class KafkaMessageConsumer {

    @Autowired
    private DeliveryOptyService deliveryOptyService;

    @KafkaListener(topics = "delivery_order_request", containerFactory = "deliveryMessageKafkaListenerContainerFactory")
    public void listener(final DeliveryMessage deliveryMessage) {
        String orderId = deliveryMessage.getId();
        log.info("Доставка заказа Id = {}", orderId);
        deliveryOptyService.createDeliveryOpty(orderId, deliveryMessage.getFio(), deliveryMessage.getPhone(), deliveryMessage.getAddress());
    }

}
