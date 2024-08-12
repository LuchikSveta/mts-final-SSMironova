package ru.mts.siebel.assembly.service;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mts.siebel.assembly.constant.StatusConstant;
import ru.mts.siebel.assembly.entity.AssemblyOpty;
import ru.mts.siebel.assembly.repository.AssemblyOptyRepository;
import ru.mts.siebel.model.StatusMessage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class AssemblyOptyService {

    @Autowired
    private AssemblyOptyRepository repository;
    @Autowired
    private KafkaTemplate<String, StatusMessage> kafkaTemplate;

    public List<AssemblyOpty> findAll() {
        return repository.findAll();
    }

    public List<AssemblyOpty> findNew() {
        return repository.findByStatus(StatusConstant.NEW);
    }

    public int size() {
        return findAll().size();
    }

    @Transactional
    public void createAssemblyOpty(final String orderId) {
        AssemblyOpty assemblyOpty = new AssemblyOpty(
                orderId,
                LocalDate.now(),
                orderId,
                StatusConstant.NEW);
        repository.save(assemblyOpty);
        log.info("Создана заявка Id = {} на сборку", assemblyOpty.getId());
        log.info("Ожидайте сборку заказа Id = {}", orderId);
    }

    @Transactional
    public void addAssemblyOpty(final AssemblyOpty assemblyOpty) {
        assemblyOpty.setId(UUID.randomUUID().toString());
        repository.save(assemblyOpty);
        log.info("Создана заявка Id = {} на сборку", assemblyOpty.getId());
    }

    public void updateAssemblyOrderStatus(final String id, final String status) {
        Optional<AssemblyOpty> orderOptional = repository.findById(id);
        if (orderOptional.isPresent()) {
            AssemblyOpty assemblyOpty = orderOptional.get();
            String oldStatus = assemblyOpty.getStatus();
            String orderId = assemblyOpty.getOrderId();
            assemblyOpty.setStatus(status);
            repository.save(assemblyOpty);
            log.info("Статус заявки на сборку Id = {} обновлен. Было: {}, стало: {}", assemblyOpty.getId(), oldStatus, status);

            kafkaTemplate.send("assembly_order_response", orderId, new StatusMessage(orderId, status));
        } else {
            log.info("Заявка на сборку Id = {} не найдена", id);
        }
    }

}
