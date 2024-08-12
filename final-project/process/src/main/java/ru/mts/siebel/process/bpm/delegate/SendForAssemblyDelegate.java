package ru.mts.siebel.process.bpm.delegate;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.mts.siebel.model.AssemblyMessage;
import ru.mts.siebel.process.bpm.constant.VariablesConstant;

@Log4j2
@Component
public class SendForAssemblyDelegate implements JavaDelegate {

    @Autowired
    private KafkaTemplate<String, AssemblyMessage> kafkaTemplate;

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        String orderId = execution.getVariable(VariablesConstant.ORDER_ID).toString();
        String status = execution.getVariable(VariablesConstant.STATUS).toString();
        AssemblyMessage assemblyMessage = new AssemblyMessage(orderId, status);
        log.info("Заявка на сборку заказа Id = {} принята", orderId);
        kafkaTemplate.send("assembly_order_request", orderId, assemblyMessage);
    }

}
