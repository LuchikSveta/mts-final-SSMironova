package ru.mts.siebel.order.service;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mts.siebel.order.entity.Order;
import ru.mts.siebel.order.repository.OrderRepository;
import ru.mts.siebel.model.StartMessage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private KafkaTemplate<String, StartMessage> kafkaTemplate;

    public List<Order> findAll() {
        return repository.findAll();
    }

    public int size() {
        return findAll().size();
    }

    @Transactional
    public void addOrder(final Order order) {
        String orderId = UUID.randomUUID().toString();
        order.setId(orderId);
        order.setCreated(LocalDate.now());
        repository.save(order);
        log.info("Создан заказ Id = {}", orderId);
        StartMessage startMessage = new StartMessage(orderId, order.getProductCode(), order.getQuantity(), order.getStatus(), order.getFio(), order.getPhone(), order.getAddress());
        kafkaTemplate.send("start_process", orderId, startMessage);
    }

    public void updateOrderStatus(final String id, final String status) {
        Optional<Order> orderOptional = repository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            String oldStatus = order.getStatus();
            order.setStatus(status);
            repository.save(order);
            log.info("Статус заказа Id = {} обновлен. Было: {}, стало: {}", order.getId(), oldStatus, status);
        } else {
            log.info("Заказ Id = {} не найден", id);
        }
    }

}
