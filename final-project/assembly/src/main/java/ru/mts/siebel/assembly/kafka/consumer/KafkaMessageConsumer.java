package ru.mts.siebel.assembly.kafka.consumer;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mts.siebel.assembly.service.AssemblyOptyService;
import ru.mts.siebel.model.AssemblyMessage;

@Log4j2
@Service
public class KafkaMessageConsumer {

    @Autowired
    private AssemblyOptyService assemblyOptyService;

    @KafkaListener(topics = "assembly_order_request", containerFactory = "assemblyMessageKafkaListenerContainerFactory")
    public void listener(final AssemblyMessage assemblyMessage) {
        String orderId = assemblyMessage.getId();
        log.info("Сборка заказа Id = {}", orderId);
        assemblyOptyService.createAssemblyOpty(orderId);
    }

}
